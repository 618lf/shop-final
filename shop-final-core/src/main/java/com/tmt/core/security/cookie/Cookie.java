package com.tmt.core.security.cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 仿照 shiro 的 cookie 管理
 * @author lifeng
 */
public interface Cookie {

	/**
     * The value of deleted cookie (with the maxAge 0).
     */
    public static final String DELETED_COOKIE_VALUE = "deleteMe";
    

    /**
     * The number of seconds in one year (= 60 * 60 * 24 * 365).
     */
    public static final int ONE_YEAR = 60 * 60 * 24 * 365;

    /**
     * Root path to use when the path hasn't been set and request context root is empty or null.
     */
    public static final String ROOT_PATH = "/";

    String getName();

    void setName(String name);

    String getValue();

    void setValue(String value);

    String getComment();

    void setComment(String comment);

    String getDomain();

    void setDomain(String domain);

    int getMaxAge();

    void setMaxAge(int maxAge);

    String getPath();

    void setPath(String path);

    boolean isSecure();

    void setSecure(boolean secure);

    int getVersion();

    void setVersion(int version);

    void setHttpOnly(boolean httpOnly);

    boolean isHttpOnly();

    void saveTo(HttpServletRequest request, HttpServletResponse response);

    void removeFrom(HttpServletRequest request, HttpServletResponse response);
}