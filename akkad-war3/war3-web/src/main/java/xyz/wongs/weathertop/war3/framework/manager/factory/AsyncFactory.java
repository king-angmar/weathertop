package xyz.wongs.weathertop.war3.framework.manager.factory;


import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import xyz.wongs.weathertop.base.constant.Constants;
import xyz.wongs.weathertop.base.utils.ServletUtils;
import xyz.wongs.weathertop.base.utils.SpringContextHolder;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.war3.common.utils.AddressUtils;
import xyz.wongs.weathertop.war3.common.utils.LogUtils;
import xyz.wongs.weathertop.war3.common.utils.ShiroUtils;
import xyz.wongs.weathertop.war3.framework.shiro.session.OnlineSession;
import xyz.wongs.weathertop.war3.system.entity.SysLoginInfo;
import xyz.wongs.weathertop.war3.system.entity.SysOperLog;
import xyz.wongs.weathertop.war3.system.entity.SysUserOnline;
import xyz.wongs.weathertop.war3.system.service.SysLoginInfoService;
import xyz.wongs.weathertop.war3.system.service.SysOperLogService;
import xyz.wongs.weathertop.war3.system.service.SysUserOnlineService;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author WCNGS@QQ.COM
 * @ClassName AsyncFactory
 * @Description 异步工厂（产生任务用）
 * @Github <a>https://github.com/rothschil</a>
 * @date 2020/6/19 17:37
 * @Version 1.0.0
 */
@Slf4j
public class AsyncFactory {

    /**
     * 同步session到数据库
     *
     * @param session 在线用户会话
     * @return 任务task
     */
    public static TimerTask syncSessionToDb(final OnlineSession session) {
        return new TimerTask() {
            @Override
            public void run() {
                SysUserOnline online = new SysUserOnline();
                online.setSessionId(String.valueOf(session.getId()));
                online.setDeptName(session.getDeptName());
                online.setLoginName(session.getLoginName());
                online.setStartTimestamp(session.getStartTimestamp());
                online.setLastAccessTime(session.getLastAccessTime());
                online.setExpireTime(session.getTimeout());
                online.setIpaddr(session.getHost());
                online.setLoginLocation(AddressUtils.getRealAddressByIP(session.getHost()));
                online.setBrowser(session.getBrowser());
                online.setOs(session.getOs());
                online.setStatus(session.getStatus());
                SpringContextHolder.getBean(SysUserOnlineService.class).insert(online);

            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringContextHolder.getBean(SysOperLogService.class).insert(operLog);
            }
        };
    }

    /**
     * 记录登陆信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message, final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = ShiroUtils.getIp();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                log.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginInfo sysLoginInfo = new SysLoginInfo();
                sysLoginInfo.setLoginName(username);
                sysLoginInfo.setIpaddr(ip);
                sysLoginInfo.setLoginLocation(address);
                sysLoginInfo.setBrowser(browser);
                sysLoginInfo.setOs(os);
                sysLoginInfo.setMsg(message);
                sysLoginInfo.setLoginTime(new Date());
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    sysLoginInfo.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    sysLoginInfo.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringContextHolder.getBean(SysLoginInfoService.class).insert(sysLoginInfo);
            }
        };
    }
}
