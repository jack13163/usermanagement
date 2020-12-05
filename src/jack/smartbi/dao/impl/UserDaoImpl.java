package jack.smartbi.dao.impl;

import jack.smartbi.dao.UserDao;
import jack.smartbi.entity.User;
import jack.smartbi.util.DBUtil;
import java.util.Arrays;
import java.util.List;

public class UserDaoImpl implements UserDao {
    /**
     * 查询用户信息
     * @param uname
     * @param pwd
     * @return
     */
    @Override
    public User getUserInfo(String uname, String pwd) {
        //创建sql命令
        String sql = "select uid, uname, pwd, salt, email, status from t_user where uname=? and pwd=?";
        //给占位符赋值
        List<Object> params = Arrays.asList(uname, pwd);
        //创建SQL命令对象
        User u = DBUtil.selectSimpleResult(sql, params, User.class);
        return u;
    }

    /**
     * 用户注册
     * @param uname     用户名
     * @param pwd       密码
     * @param salt      盐值
     * @param email     邮箱
     * @return
     */
    @Override
    public int regUserInfo(String uname, String pwd, String salt, String email) {
        // 创建sql语句: uname, pwd, salt, email, active, status
        String sql = "insert into t_user(uname, pwd, salt, email) values(?,?,?,?)";
        return DBUtil.executeDML(sql, uname, pwd, pwd, email);
    }

    /**
     * 获取用户列表信息
     * @return      用户列表
     */
    @Override
    public List<User> getUserInfoList() {
        //创建sql命令
        String sql = "select uid, uname, pwd, salt, email, status from t_user;";
        //给占位符赋值
        List<Object> params = Arrays.asList();
        //创建SQL命令对象
        List<User> userList = DBUtil.selectMoreResult(sql, params, User.class);
        return userList;
    }

    /**
     * 启用用户， -1:未通过邮箱认证 0:未锁定状态; -2:代表锁定状态
     * @param uid       用户编号
     * @return
     */
    @Override
    public int enableUser(int uid) {
        String sql = "update t_user set status = 0 where uid = ?";
        return DBUtil.executeDML(sql, uid);
    }

    /**
     * 禁用用户
     * @param uid       用户编号
     * @return
     */
    @Override
    public int disableUser(int uid) {
        String sql = "update t_user set status = -2 where uid = ?";
        return DBUtil.executeDML(sql, uid);
    }

    /**
     * 激活用户
     * @param uid       用户编号
     * @return
     */
    @Override
    public int activeUser(int uid) {
        //创建sql语句
        String sql = "update t_user set status = 0 where status = -1 and uid = ?";
        return DBUtil.executeDML(sql, uid);
    }

    /**
     * 通过用户名获取用户信息
     * @param uname         用户名
     * @return
     */
    @Override
    public User getUserInfoByName(String uname) {
        //创建sql命令
        String sql = "select uid, uname, pwd, salt, email, status from t_user where uname=?;";
        //给占位符赋值
        List<Object> params = Arrays.asList(uname);
        //创建SQL命令对象
        User u = DBUtil.selectSimpleResult(sql, params, User.class);
        return u;
    }

    /**
     * 删除用户
     * @param uid       用户编号
     */
    @Override
    public void deleteUser(int uid) {
        String sql = "delete from t_user where uid = ?";
        DBUtil.executeDML(sql, uid);
    }
}
