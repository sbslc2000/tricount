package goorm.tricount.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"isSuccess","message","result"})
public class BaseResponse<T> extends BaseResponseTemplate<T>{

    private static final String DEFAULT_MESSAGE = "요청에 성공하였습니다.";

    private BaseResponse() {
        super(true,DEFAULT_MESSAGE);
    }
    private BaseResponse(T result) {
        super(result,true, DEFAULT_MESSAGE);
    }

    private BaseResponse(T result, String message) {
        super(result, true, message);
    }

    public static <T> BaseResponse<T> ok() { return new BaseResponse<>(null);}
    public static <T> BaseResponse<T> ok(T result) {
    	return new BaseResponse<>(result);
    }
    public static <T> BaseResponse<T> ok(T result, String message) {
        return new BaseResponse<>(result,message);
    }

    @Override
    public String toString() {
        return "{" +
                "result=" + getResult() +
                ", isSuccess=" + isIsSuccess() +
                ", message='" + getMessage() +
                '}';
    }
}