package cn.sichu.miaosha.error;

/**
 * 
 * @author sichu
 * @date 2022/04/12
 */
// 包装器业务异常类实现
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    // 直接接收EnumError的传参，用于构造业务异常
    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    // 接受自定义errorMsg的方式构造业务异常
    public BusinessException(CommonError commonError, String errorMsg) {
        super();
        this.commonError = commonError;
        this.commonError = setErrorMsg(errorMsg);
    }

    @Override
    public int getErrorCode() {

        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {

        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {

        this.commonError.setErrorMsg(errorMsg);
        return this;
    }

}
