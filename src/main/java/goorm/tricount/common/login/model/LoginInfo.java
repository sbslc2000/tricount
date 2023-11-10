package goorm.tricount.common.login.model;

import goorm.tricount.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginInfo {

    private Long id;
    private String username;
    private String nickname;
    private LocalDateTime loginTime;
    private LocalDateTime lastAccessTime;

    public LoginInfo(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.loginTime = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
    }
}
