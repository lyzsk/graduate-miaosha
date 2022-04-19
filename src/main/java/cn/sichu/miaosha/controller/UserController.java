package cn.sichu.miaosha.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;

import cn.sichu.miaosha.controller.viewobject.UserVO;
import cn.sichu.miaosha.error.BusinessException;
import cn.sichu.miaosha.error.EnumError;
import cn.sichu.miaosha.response.CommonReturnType;
import cn.sichu.miaosha.service.UserService;
import cn.sichu.miaosha.service.model.UserModel;

/**
 * "@CrossOrigin"跨域, 但是在ajax请求中没法做到共享，所以inSessionOtpCode不能正确传到
 * <p>
 * 解决办法: 设置crossorigin的范围
 * <p>
 * "@CrossOrigin" 源码中的DEFAULT_ALLOWED_HEADERS允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求(@deprecated as of
 * Spring 5.0,)
 * 
 * @author sichu
 * @date 2022/04/12
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * httpServletRequest 通过 bean 的方式注入进来， 是一个单例模式， 如何支持多个用户的并发访问？ httpServletRequest 本质是一个 proxy， 内部有threadLocal 方式的
     * map， 让用户在每个 thread 中处理自己的 request，并且有 threadLocal的清除方式
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 用户注册接口
     * 
     * @throws BusinessException
     */
    @RequestMapping(value = "/loginRegister", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telephone") String telephone,
        @RequestParam(name = "otpCode") String otpCode, @RequestParam(name = "name") String name,
        @RequestParam(name = "gender") Integer gender, @RequestParam(name = "age") Integer age,
        @RequestParam(name = "password") String password) throws BusinessException {

        // 验证手机号和对应的 otpcode 相符合
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
        if (StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EnumError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }

        // 用户注册流程
        // 需要添加对应的 Service
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        // 防止明文传password
        userModel.setEncryptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) {
        // 确定计算方法
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Base64 base64 = null;
        String newstr = base64.encodeBase64String(md5.digest(str.getBytes()));
        return newstr;
    }

    /**
     * 用户获取otp短信接口
     * 
     * @param telephone
     * @return
     */
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telephone") String telephone) {

        // 按照一定规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        // 将OTP验证码对应用户的手机号关联，(企业里面用Redis对应)暂时用httpsession筛选，
        // 绑定 手机号<---->otpCode
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        // 将OTP验证码通过短信通道发送给用户，省略，HTTP POST 给用户手机号码
        System.out.println("telephone = " + telephone + " && otpCode = " + otpCode);

        // return
        return CommonReturnType.create(null);
    }

    /**
     * 设置ResponseBody, 返回userModel把业务处理的Model领域模型返回给前端是sb行为
     * 
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        // 调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        // 若获取的用户信息不存在
        if (userModel == null) {
            throw new BusinessException(EnumError.USER_NOT_EXIST);
            // userModel.setEncryptPassword("123");
        }

        // 将核心领域模型用户对象转换成可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);

        // 返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

}
