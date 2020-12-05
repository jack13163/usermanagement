package jack.smartbi.service;

public interface UserLoginInfoService {
    /**
     * 根据用户登陆日志表判断是否满足锁定用户的条件
     * @param uid           用户id
     * @param timeLen       时间长度(分钟)
     * @param errorTimes    错误次数
     * @return
     */
    public boolean checkPassWrongTime(int uid, int timeLen, int errorTimes);

    /**
     * 记录登陆日志信息
     * @param uid           用户编号
     * @param b             登陆成功还是失败
     * @param remoteAddr    远程客户端IP地址
     */
    void recordLoginLog(int uid, boolean b, String remoteAddr);
}
