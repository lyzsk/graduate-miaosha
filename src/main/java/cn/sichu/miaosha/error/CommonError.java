package cn.sichu.miaosha.error;

/**
 * 
 * @author sichu
 * @date 2022/04/12
 */
public interface CommonError {
    public int getErrorCode();

    public String getErrorMsg();

    public CommonError setErrorMsg(String errorMsg);
}
