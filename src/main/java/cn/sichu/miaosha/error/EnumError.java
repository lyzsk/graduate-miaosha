package cn.sichu.miaosha.error;

/**
 * 
 * @author sichu
 * @date 2022/04/12
 */
public enum EnumError implements CommonError {
    // 通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    // 未知错误
    UNKNOWN_ERROR(10002, "未知错误"),
    // 20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001, "用户不存在");

    private EnumError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private int errorCode;
    private String errorMsg;

    @Override
    public int getErrorCode() {

        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {

        return this.errorMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

}
