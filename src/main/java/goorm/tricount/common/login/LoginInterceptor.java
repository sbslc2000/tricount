package goorm.tricount.common.login;

import goorm.tricount.api.ErrorCode;
import goorm.tricount.api.exception.ClientFaultException;
import goorm.tricount.common.login.model.LoginInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        LoginInfo loginInfo = (LoginInfo)session.getAttribute("loginInfo");
        if(loginInfo == null) {
            throw new ClientFaultException(ErrorCode.LOGIN_REQUIRED);
        } else {
            loginInfo.setLastAccessTime(LocalDateTime.now());
            loginInfo.setLoginTime(null);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
