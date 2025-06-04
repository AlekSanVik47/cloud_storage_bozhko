package ru.cloudstorage.cloud_storage_core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bozhko
 * для заполнения формы регистрации
 */
@Data
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "Имя пользователя должно быть заполнено")
    private String userName;
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;
    @NotBlank(message = "Введите пароль еще раз")
    private String matchingPassword;
    private String firstName;
    private String lastName;
    @Email(message = "Неверный формат электронной почты")
    private String email;
}