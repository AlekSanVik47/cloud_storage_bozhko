package controllers;

import configarations.TestApplication;
import lombok.Getter;
import lombok.Setter;
import org.junit.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import redis.clients.jedis.Jedis;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertFalse;

@Getter
@Setter
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestApplication.class)
public class SessionControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String testUrl;
    private Jedis jedis;

    @Before
    public void setUp() {
      this.testUrl="http://localhost:"+port;
      this.jedis = new Jedis("localhost", 6379);
    }

    @After
    public void tearDown() {
        if (jedis != null) {
            jedis.close();
        }
    }

    @Test
    public void testRedisIsEmpty() {
        Set<String> result = jedis.keys("*");
        assertTrue(result.isEmpty(), "Redis должен быть пустым после очистки");
    }


    @Test
    public void testUnauthenticatedCantAccess() {
        ResponseEntity<String> result = restTemplate.getForEntity(testUrl, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    public void testRedisControlsSession() {
        // 1. Авторизованный запрос
        TestRestTemplate authTemplate = new TestRestTemplate("admin", "password");
        ResponseEntity<String> result = authTemplate.getForEntity(testUrl, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        // 2. Проверка Redis
        Set<String> redisResult = jedis.keys("*");
        assertFalse("Redis должен содержать данные сессии", redisResult.isEmpty());

        // 3. Извлечение куки
        String sessionCookie = result.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(sessionCookie);
        sessionCookie = sessionCookie.split(";")[0];

        // 4. Запрос с куки
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionCookie);
        ResponseEntity<String> cookieResponse = restTemplate.exchange(
                testUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.OK, cookieResponse.getStatusCode());

        // 5. Очистка Redis и проверка
        jedis.flushAll();
        ResponseEntity<String> failedResponse = restTemplate.exchange(
                testUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, failedResponse.getStatusCode());
    }
}
