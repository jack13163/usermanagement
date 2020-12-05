package jack.smartbi.dao.impl;

import jack.smartbi.dao.UserLoginInfoDao;
import jack.smartbi.util.DBUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 用户登录信息DAO层实现
 */
public class UserLoginInfoDaoImpl implements UserLoginInfoDao {

    /**
     * 根据用户登陆日志表判断是否满足锁定用户的条件
     * @param uid           用户id
     * @param timeLen       时间长度
     * @param errorTimes    错误次数
     * @return
     */
    @Override
    public boolean checkPassWrongTime(int uid, int timeLen, int errorTimes) {

        // 创建子查询sql命令：pass_wrong_time_status，logintime，ipaddr，id，uid
        String sql = "select sum(tab.pass_wrong_time_status) as res from " +
                " (select pass_wrong_time_status, logintime from t_user_login order by logintime desc limit " +
                errorTimes + ") as tab " +
                "where tab.logintime > DATE_ADD(NOW(), INTERVAL - " + timeLen + " MINUTE);";
        // 给占位符赋值
        List<Object> params = Arrays.asList();
        // 创建SQL命令对象
        Object res = DBUtil.selectSimpleResult(sql, params, Integer.class);

        if(res != null && Integer.parseInt(res.toString()) == 6){
            return true;
        }
        return false;
    }

    /**
     * 记录用户登陆日志
     * @param uid           用户id
     * @param code          状态码：0代表成功，2代表失败
     * @param remoteAddr    远程客户端IP地址
     */
    @Override
    public void recordLoginLog(int uid, int code, String remoteAddr) {

        // 创建sql命令：pass_wrong_time_status，logintime，ipaddr，id，uid
        String sql = "insert into t_user_login(uid, ipaddr, pass_wrong_time_status) values(?,?,?);";
        // 创建SQL命令对象
        DBUtil.executeDML(sql, uid, remoteAddr, code);
    }
}
