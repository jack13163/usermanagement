package jack.smartbi.service;


import jack.smartbi.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 用户登陆
     * @param uname
     * @param pwd
     * @return
     */
    User getUserInfo(String uname, String pwd);

    /**
     * 用户注册
     * @param uname
     * @param pwd
     * @param salt
     * @param email
     * @return
     */
    int regUserInfo(String uname, String pwd, String salt, String email);

    /**
     * 获取用户列表
     * @return
     */
    List<User> getUserList();

    /**
     * 激活用户
     * @param uid 用户的编号
     * @return
     */
    int activeUser(int uid);

    /**
     * 通过用户名获取用户
     * @param uname
     * @return
     */
    User getUserInfoByName(String uname);

    /**
     * 禁用用户
     * @param uid       用户编号
     * @return
     */
    void disableUser(int uid);

    /**
     * 开启用户
     * @param uid       用户编号
     * @return
     */
    void enableUser(int uid);

    /**
     * 删除用户
     * @param uid       用户编号
     */
    void deleteUser(int uid);
}
