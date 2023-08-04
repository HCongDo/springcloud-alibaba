package com.study.common.entity;

/**
 *  自定义上下文用户信息
 * @Description: TODO
 * @Author hecong
 * @Date 2023/8/4 19:27
 * @Version 1.0
 */
public class UserContextHolder {

  private ThreadLocal<JwtInfo> threadLocal;

  private UserContextHolder() {
    this.threadLocal = new ThreadLocal();
  }

  public static UserContextHolder getInstance() {
    return UserContextHolder.SingletonHolder.sInstance;
  }

  public void setContext(JwtInfo claims) {
    this.threadLocal.set(claims);
  }

  public JwtInfo getContext() {
    return this.threadLocal.get();
  }

  public void clear() {
    this.threadLocal.remove();
  }

  private static class SingletonHolder {

    private static final UserContextHolder sInstance = new UserContextHolder();

    private SingletonHolder() {
    }
  }
}

