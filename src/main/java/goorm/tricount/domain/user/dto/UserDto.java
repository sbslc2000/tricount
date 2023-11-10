package goorm.tricount.domain.user.dto;


import goorm.tricount.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String nickname;

    public static UserDto of(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getNickname()
        );
    }
}
