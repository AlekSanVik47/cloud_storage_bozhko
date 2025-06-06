package dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(name = "AuthRequest", description = "Объект авторизации")
public class AuthRequest {
    @Schema(name = "email", example = "admin", description = "Логин")
    public String email;
    @Schema(name = "password", example = "admin", description = "Пароль")
    public String password;

}
