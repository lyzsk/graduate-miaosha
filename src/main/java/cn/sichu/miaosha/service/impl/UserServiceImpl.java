package cn.sichu.miaosha.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sichu.miaosha.dao.UserDOMapper;
import cn.sichu.miaosha.dao.UserPassWordDOMapper;
import cn.sichu.miaosha.dataobject.UserDO;
import cn.sichu.miaosha.dataobject.UserPassWordDO;
import cn.sichu.miaosha.service.UserService;
import cn.sichu.miaosha.service.model.UserModel;

/**
 * 
 * @author sichu
 * @date 2022/04/12
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPassWordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id) {

        // 通过调用userDOMapper获取到对应的用户dataobject
        // userDO绝对不能透传给前端，userDO与数据库一一映射，并不含有任何逻辑
        // 在service层不能把映射逻辑透传，必须要有一个Model的概念
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if (userDO == null) {
            return null;
        }
        // 通过userId获取对应用户的加密密码信息
        UserPassWordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);

        // 把DO对象转成Model对象返回给Controller层
        return convertFromDataObject(userDO, userPasswordDO);
    }

    /**
     * 通过userDO,userPasswordDO组装成一个UserModel对象
     * <p>
     * 用copyProperties把userDO的属性copy到userModel内
     * <p>
     * 要注意判空，虽然不太可能传空的password
     * <p>
     * copyProperties不够智能，必须要类型一一对应
     * 
     * @param userDO
     * @param userPasswordDO
     * @return
     */
    private UserModel convertFromDataObject(UserDO userDO, UserPassWordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);

        if (userPasswordDO != null) {
            userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());
        }

        return userModel;
    }

}
