package goorm.tricount.domain.user;

import goorm.tricount.common.Entity;
import goorm.tricount.domain.settlement.model.Settlement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
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
