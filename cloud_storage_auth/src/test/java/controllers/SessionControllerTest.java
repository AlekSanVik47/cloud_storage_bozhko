package controllers;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionControllerIntegrationTest {
    private static final String BASE_URL = "http://localhost:8099";
    private static final String LOGIN_URL = BASE_URL + "/auth/login";
    private static final String PROTECTED_URL = BASE_URL + "/api/protected";

    private TestRestTemplate anonymousClient;
    private TestRestTemplate authClient;
    private Jedis jedis;

    @BeforeAll
    void init() {
        this.jedis = new Jedis("localhost", 6379);
        this.anonymousClient = new TestRestTemplate();
        this.authClient = new TestRestTemplate("admin", "admin123", null);

        clearRedisWithWait();
    }

    @AfterEach
    void tearDown() {
        clearRedisWithWait();
    }

    private void clearRedisWithWait() {
        jedis.flushAll();
        await().atMost(2, TimeUnit.SECONDS)
                .until(() -> jedis.keys("*").isEmpty());
    }

    @Test
    @DisplayName("Неавторизованный доступ к защищенному ресурсу")
    void whenAnonymousAccessProtected_thenUnauthorized() {
        ResponseEntity<String> response = anonymousClient.getForEntity(
                PROTECTED_URL,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Должен вернуть 401 для неавторизованного доступа");
    }

    @Test
    @DisplayName("Успешная аутентификация и доступ")
    void whenAuthenticated_thenCanAccessProtected() {
        // 1. Аутентификация
        ResponseEntity<String> loginResponse = authClient.getForEntity(
                LOGIN_URL,
                String.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode(),
                "Логин должен быть успешным");

        // 2. Проверка сессии в Redis
        await().atMost(1, TimeUnit.SECONDS)
                .until(() -> !jedis.keys("spring:session:*").isEmpty());

        // 3. Извлечение сессионной куки
        String sessionCookie = loginResponse.getHeaders()
                .getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(sessionCookie, "Должна вернуться сессионная кука");

        // 4. Доступ с кукой
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionCookie);

        ResponseEntity<String> protectedResponse = anonymousClient.exchange(
                PROTECTED_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.OK, protectedResponse.getStatusCode(),
                "Должен разрешить доступ с валидной сессией");
    }

    @Test
    @DisplayName("Потеря доступа после очистки сессии")
    void whenSessionCleared_thenAccessDenied() {
        // 1. Аутентификация
        ResponseEntity<String> loginResponse = authClient.getForEntity(
                LOGIN_URL,
                String.class
        );
        String sessionCookie = loginResponse.getHeaders()
                .getFirst(HttpHeaders.SET_COOKIE);

        // 2. Очистка сессий
        clearRedisWithWait();

        // 3. Попытка доступа с просроченными куками
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionCookie);

        ResponseEntity<String> response = anonymousClient.exchange(
                PROTECTED_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Должен запретить доступ после очистки сессии");
    }
}