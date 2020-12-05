package jack.smartbi.dao;

public interface UserLoginInfoDao {

    /**
     * 检查用户最近一段时间内是否登录出错很多次
     * @param uid           用户id
     * @param timeLen       时间长度
     * @param errorTimes    错误次数
     * @return
     */
    boolean checkPassWrongTime(int uid, int timeLen, int errorTimes);

    /**
     * 记录登陆日志
     * @param uid           用户id
     * @param code          状态码：0代表成功，2代表失败
     * @param remoteAddr    远程客户端IP地址
     */
    void recordLoginLog(int uid, int code, String remoteAddr);
}
