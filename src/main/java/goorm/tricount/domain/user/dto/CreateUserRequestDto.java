package goorm.tricount.domain.user.dto;

import lombok.Data;

@Data
public class CreateUserRequestDto {

    private String username;
    private String nickname;
    private String password;
}
