package cn.sichu.miaosha.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sichu.miaosha.controller.viewobject.UserVO;
import cn.sichu.miaosha.error.BusinessException;
import cn.sichu.miaosha.error.EnumError;
import cn.sichu.miaosha.response.CommonReturnType;
import cn.sichu.miaosha.service.UserService;
import cn.sichu.miaosha.service.model.UserModel;

/**
 * 
 * 
 * @author sichu
 * @date 2022/04/12
 */
@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

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
