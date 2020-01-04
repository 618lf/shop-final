package com.tmt.core.web.tld.freemarker;

import java.io.IOException;
import java.util.Map;

import com.tmt.core.security.utils.SecurityUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

public class IsRunAsTag extends SecureTag{

	@Override
	public void render(Environment env, Map<String, Object> params,
			TemplateDirectiveBody body) throws IOException, TemplateException {
		if (SecurityUtils.getSubject().isRunAs()) {
            log.debug("Subject has known identity (aka 'principal'). Tag body will be evaluated.");
            renderBody(env, body);
        } else {
            log.debug("Subject does not exist or have a known identity (aka 'principal'). Tag body will not be evaluated.");
        }
	}
}
