package cn.sichu.miaosha.dao;

import cn.sichu.miaosha.dataobject.UserPassWordDO;

public interface UserPassWordDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 12 20:19:08 BST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 12 20:19:08 BST 2022
     */
    int insert(UserPassWordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 12 20:19:08 BST 2022
     */
    int insertSelective(UserPassWordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 12 20:19:08 BST 2022
     */
    UserPassWordDO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 12 20:19:08 BST 2022
     */
    int updateByPrimaryKeySelective(UserPassWordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 12 20:19:08 BST 2022
     */
    int updateByPrimaryKey(UserPassWordDO record);
}