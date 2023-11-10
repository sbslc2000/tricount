package goorm.tricount.api;

import lombok.Getter;

@Getter
public enum ErrorCode {
    LOGIN_REQUIRED(2001,"로그인이 필요합니다."),
    LOGIN_FAILED(2002, "비밀번호가 틀립니다."),
    INVALID_REQUEST(4000, "유효하지 않은 요청입니다."),
    UNAUTHORIZED(4001, "인증되지 않은 사용자입니다."),
    FORBIDDEN(4002, "접근이 거부되었습니다."),
    PERMISSION_DENIED(4003, "권한이 없습니다."),
    VALIDATION_FAILURE(4100,"검증 오류가 발생했습니다."),
    ENTITY_NOT_FOUND(4200, "해당 엔티티를 찾을 수 없습니다."),
    JSON_PARSE_ERROR(4300, "JSON 파싱 에러가 발생하였습니다."),
    INTERNAL_SERVER_ERROR(5000, "서버 내부 에러가 발생하였습니다."),
    DATA_INTEGRITY_FAILURE(9999, "데이터 무결성 에러가 발생하였습니다."),
    USER_NOT_FOUND(10000, "존재하지 않는 사용자입니다."),
    WORKSPACE_NOT_FOUND(11000, "존재하지 않는 워크스페이스입니다."),
    WORKSPACE_ALREADY_JOINED(11001, "이미 가입된 워크스페이스입니다."),
    WORKSPACE_NOT_JOINED(11002, "워크스페이스에 가입되지 않은 사용자입니다."),
    CHANNEL_NOT_FOUND(12000, "존재하지 않는 채널입니다."),
    ;
    private final int code;
    private final String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}