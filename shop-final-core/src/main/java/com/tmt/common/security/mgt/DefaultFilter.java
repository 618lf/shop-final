package com.tmt.common.security.mgt;

import javax.servlet.Filter;

import com.tmt.common.security.filter.authc.AnonymousFilter;
import com.tmt.common.security.filter.authc.AuthenticatingFilter;
import com.tmt.common.security.filter.authc.LogoutFilter;
import com.tmt.common.security.filter.authc.UserFilter;
import com.tmt.common.security.filter.authz.IpsAuthorizationFilter;
import com.tmt.common.security.filter.authz.PermissionsAuthorizationFilter;
import com.tmt.common.security.filter.authz.RolesAuthorizationFilter;

public enum DefaultFilter {

	anon(AnonymousFilter.class), 
	authc(AuthenticatingFilter.class), 
	logout(LogoutFilter.class), 
	perms(PermissionsAuthorizationFilter.class), 
	roles(RolesAuthorizationFilter.class), 
	ips(IpsAuthorizationFilter.class), 
	user(UserFilter.class);

	private final Class<? extends Filter> filterClass;
	
	private DefaultFilter(Class<? extends Filter> filterClass) {
		this.filterClass = filterClass;
	}

	public Class<? extends Filter> getFilterClass() {
		return filterClass;
	}
	
	public Filter newInstance() throws InstantiationException, IllegalAccessException {
        return (Filter) filterClass.newInstance();
    }
}
