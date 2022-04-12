package cn.sichu.miaosha.service;

import cn.sichu.miaosha.service.model.UserModel;

/**
 * UserService的返回值是一个Model对象
 * 
 * @author sichu
 * @date 2022/04/12
 */
public interface UserService {
    // 通过用户ID获取用户对象的方法
    public UserModel getUserById(Integer id);
}
