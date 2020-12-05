package jack.smartbi.service.impl;

import jack.smartbi.dao.UserDao;
import jack.smartbi.dao.impl.UserDaoImpl;
import jack.smartbi.entity.User;
import jack.smartbi.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    /**
     * 检验用户名和密码是否正确
     * @param uname
     * @param pwd
     * @return
     */
    @Override
    public User getUserInfo(String uname, String pwd) {
        // 处理登录业务
        return userDao.getUserInfo(uname, pwd);
    }

    /**
     * 用户注册
     *
     * @param uname 用户名
     * @param pwd   用户密码
     * @param salt  盐值
     * @param email 邮箱
     * @return
     */
    @Override
    public int regUserInfo(String uname, String pwd, String salt, String email) {
        // 默认创建用户的时候不启用，需要进行邮箱激活后，由管理员进行启用
        return userDao.regUserInfo(uname, pwd, pwd, email);
    }

    /**
     * 获取用户列表
     * @return
     */
    @Override
    public List<User> getUserList() {
        return userDao.getUserInfoList();
    }

    /**
     * 激活用户
     * @param uid 用户的编号
     * @return
     */
    @Override
    public int activeUser(int uid) {
        return userDao.activeUser(uid);
    }

    /**
     * 通过用户名获取用户姓名
     * @param uname
     * @return
     */
    @Override
    public User getUserInfoByName(String uname) {
        return userDao.getUserInfoByName(uname);
    }

    /**
     * 禁用用户
     * @param uid       用户编号
     */
    @Override
    public void disableUser(int uid) {
        userDao.disableUser(uid);
    }

    /**
     * 启用用户
     * @param uid       用户编号
     */
    @Override
    public void enableUser(int uid) {
        userDao.enableUser(uid);
    }

    /**
     * 删除用户
     * @param uid       用户编号
     */
    @Override
    public void deleteUser(int uid) {
        userDao.deleteUser(uid);
    }
}
