package cn.sichu.miaosha.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sichu.miaosha.dao.UserDOMapper;
import cn.sichu.miaosha.dao.UserPassWordDOMapper;
import cn.sichu.miaosha.dataobject.UserDO;
import cn.sichu.miaosha.dataobject.UserPassWordDO;
import cn.sichu.miaosha.error.BusinessException;
import cn.sichu.miaosha.error.EnumError;
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

    /**
     * 防止出现 插入UserDO,没插进UserPassWordDO的情况，用"@Transactional"注册
     * 
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {

        if (userModel == null) {
            throw new BusinessException(EnumError.PARAMETER_VALIDATION_ERROR);
        }
        if (StringUtils.isEmpty(userModel.getName()) || userModel.getGender() == null || userModel.getAge() == null
            || StringUtils.isEmpty(userModel.getTelephone())) {
            throw new BusinessException(EnumError.PARAMETER_VALIDATION_ERROR);
        }

        // 实现model->dataobject方法
        // insertSelective() 而不是 insert()，因为前者有判空跳过，意义在于完全依赖数据库，数据库有什么值就用什么值，并且有update覆盖的作用
        UserDO userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EnumError.PARAMETER_VALIDATION_ERROR, "duplicated telephone");
        }
        // userDOMapper.insertSelective(userDO);
        UserPassWordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    private UserDO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserDO userDo = new UserDO();
        BeanUtils.copyProperties(userModel, userDo);
        return userDo;
    }

    private UserPassWordDO convertPasswordFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPassWordDO userPasswordDO = new UserPassWordDO();
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;

    }

}
