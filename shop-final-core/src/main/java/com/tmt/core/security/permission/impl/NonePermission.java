package com.tmt.core.security.permission.impl;

import java.util.Set;

import com.tmt.core.security.permission.Permission;

/**
 * 无权限
 *
 * @author lifeng
 */
public enum NonePermission implements Permission {

    /**
     * 无权限
     */
    INSTANCE;

    @Override
    public boolean implies(Set<String> permissions) {
        return true;
    }
}
