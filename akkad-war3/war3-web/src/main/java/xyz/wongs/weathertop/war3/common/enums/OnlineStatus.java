package xyz.wongs.weathertop.war3.common.enums;

/**
 * @ClassName OnlineStatus
 * @Description 用户会话
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 18:07
 * @Version 1.0.0
*/
public enum OnlineStatus {
    /**
     * 用户状态
     */
    on_line("在线"), off_line("离线");

    private final String info;

    private OnlineStatus(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
