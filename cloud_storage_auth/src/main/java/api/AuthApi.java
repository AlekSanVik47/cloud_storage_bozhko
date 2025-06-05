package api;

import dto.request.AuthRequest;
import dto.request.RegistrationRequest;
import dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication API", description = "Управление аутентификацией и регистрацией")
public interface AuthApi {
    @Operation(summary = "Вход в систему")
    @ApiResponse(responseCode = "200", description = "Успешная аутентификация")
    @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request);

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    ResponseEntity<AuthResponse> register(@RequestBody RegistrationRequest request);
}