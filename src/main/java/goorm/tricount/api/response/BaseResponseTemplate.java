package goorm.tricount.api.response;

public abstract class BaseResponseTemplate<T> {
    private T result;
    private boolean isSuccess;
    private String message;

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }

    public BaseResponseTemplate(T result, boolean isSuccess, String message) {
        this.result = result;
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public BaseResponseTemplate(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}