
package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым.")
    @Length(max = 50, message = "Имя пользователя не должно превышать 50 символов.")
    private String name;

    @Email(message = "Некорректный формат почтового адреса.")
    @NotBlank(message = "Почта пользователя не может быть пустой.")
    private String email;

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}