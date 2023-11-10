package goorm.tricount.common.login;

import goorm.tricount.api.response.BaseResponse;
import goorm.tricount.common.login.model.LoginInfo;
import goorm.tricount.domain.user.User;
import goorm.tricount.domain.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {


    @Data
    public static class LoginRequest{
        private String username;
        private String password;
    }

    private final UserService userService;

    @PostMapping("/login")
    public BaseResponse<Void> login(@RequestBody LoginRequest loginRequest, HttpSession session) {

        User user = userService.getUserByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());

        session.setAttribute("loginInfo", new LoginInfo(user));
        return BaseResponse.ok();
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginInfo");
        return "redirect:/";
    }

}
