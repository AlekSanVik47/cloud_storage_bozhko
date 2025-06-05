package dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(name = "RegistrationRequest", description = "Регистрация пользователя")
public class RegistrationRequest {
    @Schema(description = "Имя пользователя", example = "Иван")
    String firstName;
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    String lastName;
    @Schema(description = "Почта пользователя", example = "ivan@mail.ru")
    String email;

}
