package goorm.tricount.api.exception;

import goorm.tricount.api.ErrorCode;

public class ServerFaultException extends BaseException {
    public ServerFaultException(ErrorCode errorCode, String message) {
        super(errorCode,message);
    }
    public ServerFaultException(ErrorCode errorCode) {
        super(errorCode);
    }
}