package jack.smartbi.service.impl;

import jack.smartbi.dao.UserLoginInfoDao;
import jack.smartbi.dao.impl.UserLoginInfoDaoImpl;
import jack.smartbi.service.UserLoginInfoService;

/**
 * 登陆日志服务层
 */
public class UserLoginInfoServiceImpl implements UserLoginInfoService {

    UserLoginInfoDao userLoginInfoDao = new UserLoginInfoDaoImpl();

    /**
     * 检查是否需要锁定用户
     * @param uid           用户id
     * @param timeLen       时间长度
     * @param errorTimes    错误次数
     * @return
     */
    @Override
    public boolean checkPassWrongTime(int uid, int timeLen, int errorTimes) {
        return userLoginInfoDao.checkPassWrongTime(uid, timeLen, errorTimes);
    }

    /**
     * 记录登陆日志
     * @param uid           用户编号
     * @param b             登陆成功还是失败
     * @param remoteAddr    远程客户端IP地址
     */
    @Override
    public void recordLoginLog(int uid, boolean b, String remoteAddr) {
        // 登陆成功为0，失败为2
        int code = b ? 0 : 2;
        userLoginInfoDao.recordLoginLog(uid, code, remoteAddr);
    }
}
