package club.sword.community.exception;

public class CustomizeException extends RuntimeException {//extends继承运行时的异常类
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {//传入接口
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
