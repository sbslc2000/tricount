package goorm.tricount.domain.user;

import goorm.tricount.common.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User extends Entity {

    public User(Long id, String username, String password, String nickname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    private String username;
    private String password;
    private String nickname;
}
