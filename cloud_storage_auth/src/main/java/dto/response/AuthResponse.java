package dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "AuthResponse", description = "Ответ при успешной авторизации пользователя")
public class AuthResponse {
    @Schema(description = "Имя пользователя", example = "user")
    String username;
}
