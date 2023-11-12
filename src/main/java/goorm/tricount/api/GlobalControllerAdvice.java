package goorm.tricount.api;

import goorm.tricount.api.ErrorCode;
import goorm.tricount.api.exception.BaseException;
import goorm.tricount.api.exception.ClientFaultException;
import goorm.tricount.api.response.BaseErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"goorm.tricount.domain", "goorm.tricount.common"})
@RequiredArgsConstructor
@Order(1)
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClientFaultException.class)
    protected BaseErrorResponse<?> handleClientFaultException(ClientFaultException e) {
        return new BaseErrorResponse<>(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected BaseErrorResponse<?> handleException(Exception e) {
        e.printStackTrace();
        return new BaseErrorResponse<>(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public BaseErrorResponse<?> handleEmptyDataAccessException(EmptyResultDataAccessException ex) {
        return new BaseErrorResponse<>(ErrorCode.ENTITY_NOT_FOUND);
    }
}
