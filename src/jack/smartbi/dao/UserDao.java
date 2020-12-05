package jack.smartbi.dao;

import jack.smartbi.entity.User;
import jack.smartbi.util.DBUtil;

import java.util.List;

public interface UserDao {

    User getUserInfo(String uname, String pwd);

    int regUserInfo(String uname, String pwd, String salt, String email);

    List<User> getUserInfoList();

    /**
     * 启用用户
     * @param uid       用户编号
     * @return
     */
    int enableUser(int uid);

    /**
     * 禁用用户
     * @param uid       用户编号
     * @return
     */
    int disableUser(int uid);

    /**
     * 邮箱验证用户信息，及更新
     * @param uid       用户编号
     * @return
     */
    int activeUser(int uid);

    /**
     * 通过用户名获取用户信息
     * @param uname         用户名
     * @return
     */
    User getUserInfoByName(String uname);

    /**
     * 删除用户
     * @param uid       用户编号
     */
    void deleteUser(int uid);
}
