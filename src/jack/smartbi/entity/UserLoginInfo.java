package jack.smartbi.entity;

/**
 * 用户登录信息，对应于数据库中的t_user_login表
 */
public class UserLoginInfo {
    private int id;
    // 用户编号
    private int uid;
    private String ipAddr;
    private String loginTime;
    // '登陆密码错误状态: 0 正确 2错误
    private int statue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public UserLoginInfo() {
    }

    public UserLoginInfo(int id, int uid, String ipAddr, String loginTime, int statue) {
        this.id = id;
        this.uid = uid;
        this.ipAddr = ipAddr;
        this.loginTime = loginTime;
        this.statue = statue;
    }

    @Override
    public String toString() {
        return "UserLoginInfo{" +
                "id=" + id +
                ", uid=" + uid +
                ", ipAddr='" + ipAddr + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", statue=" + statue +
                '}';
    }
}
