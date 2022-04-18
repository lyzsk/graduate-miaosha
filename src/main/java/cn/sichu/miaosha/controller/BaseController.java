package cn.sichu.miaosha.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sichu.miaosha.error.BusinessException;
import cn.sichu.miaosha.error.EnumError;
import cn.sichu.miaosha.response.CommonReturnType;

/**
 * 存放所有Controller都要用的方法，比如Exception
 * 
 * @author sichu
 * @date 2022/04/12
 */
public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    /**
     * 定义exceptionhandler解决未被controller层吸收的exception
     * <p>
     * 这种方式没法返回Controller需要的"@ResponseBody"
     * <p>
     * 加上"@ResponseBody"就可以返回页面
     * 
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception exception) {

        Map<String, Object> responseData = new HashMap<String, Object>();
        if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException)exception;
            responseData.put("errorCode", businessException.getErrorCode());
            responseData.put("errorMsg", businessException.getErrorMsg());

        } else {
            responseData.put("errorCode", EnumError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errorMsg", EnumError.UNKNOWN_ERROR.getErrorMsg());
        }
        return CommonReturnType.create(responseData, "fail");
    }
}
