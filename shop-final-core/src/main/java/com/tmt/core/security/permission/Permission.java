package com.tmt.core.security.permission;

import java.util.Set;

import com.tmt.core.security.permission.impl.NonePermission;

/**
 * 权限标示，将 @Auth 和 路径权限配置封装为 Permission
 *
 * @author lifeng
 */
public interface Permission {

    /**
     * 无权限
     */
    Permission NONE = NonePermission.INSTANCE;

    /**
     * 是否符合这些权限
     *
     * @param permissions 权限集合
     * @return 是否符合这些权限
     */
    boolean implies(Set<String> permissions);
}
