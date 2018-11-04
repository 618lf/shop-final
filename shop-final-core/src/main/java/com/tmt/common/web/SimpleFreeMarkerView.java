package com.tmt.common.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.AbstractTemplateView;

import com.tmt.common.utils.freemarker.FreeMarkerConfig;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 轻量级的FreeemarkerView
 * 
 * 不支持jsp标签、不支持request、session、application等对象，可用于前台模板页面。
 */
public class SimpleFreeMarkerView extends AbstractTemplateView {
	private String encoding;
	private Configuration configuration;
	
	/**
	 * Set the encoding of the FreeMarker template file. Default is determined
	 * by the FreeMarker Configuration: "ISO-8859-1" if not specified otherwise.
	 * <p>Specify the encoding in the FreeMarker Configuration rather than per
	 * template if all your templates share a common encoding.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Return the encoding for the FreeMarker template.
	 */
	protected String getEncoding() {
		return this.encoding;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	protected Configuration getConfiguration() {
		return this.configuration;
	}

	/**
	 * 自动检测FreeMarkerConfig
	 * 
	 * @return
	 * @throws BeansException
	 */
	protected FreeMarkerConfig autodetectConfiguration() throws BeansException {
		try {
			return (FreeMarkerConfig) BeanFactoryUtils
					.beanOfTypeIncludingAncestors(getApplicationContext(),
							FreeMarkerConfig.class, true, false);
		} catch (NoSuchBeanDefinitionException ex) {
			throw new ApplicationContextException(
					"Must define a single FreeMarkerConfig bean in this web application context "
							+ "(may be inherited): FreeMarkerConfigurer is the usual implementation. "
							+ "This bean may be given any name.", ex);
		}
	}

	/**
	 * Invoked on startup. Looks for a single FreeMarkerConfig bean to find the
	 * relevant Configuration for this factory.
	 * <p>
	 * Checks that the template for the default Locale can be found: FreeMarker
	 * will check non-Locale-specific templates if a locale-specific one is not
	 * found.
	 * 
	 * @see freemarker.cache.TemplateCache#getTemplate
	 */
	protected void initApplicationContext() throws BeansException {
		super.initApplicationContext();
		if (getConfiguration() == null) {
			FreeMarkerConfig config = autodetectConfiguration();
			setConfiguration(config.getConfiguration());
		}
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	protected void renderMergedTemplateModel(Map model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// BASE 路径
		// model.put("base", ContextHolderUtils.getBasePath());
		
		// 实例化模板
		getConfiguration().getTemplate(getUrl()).process(model, response.getWriter());
	}
	
	/**
	 * Retrieve the FreeMarker template for the given locale,
	 * to be rendering by this view.
	 * <p>By default, the template specified by the "url" bean property
	 * will be retrieved.
	 * @param locale the current locale
	 * @return the FreeMarker template to render
	 * @throws IOException if the template file could not be retrieved
	 * @see #setUrl
	 * @see #getTemplate(String, java.util.Locale)
	 */
	protected Template getTemplate(Locale locale) throws IOException {
		return getTemplate(getUrl(), locale);
	}

	/**
	 * Retrieve the FreeMarker template specified by the given name,
	 * using the encoding specified by the "encoding" bean property.
	 * <p>Can be called by subclasses to retrieve a specific template,
	 * for example to render multiple templates into a single view.
	 * @param name the file name of the desired template
	 * @param locale the current locale
	 * @return the FreeMarker template
	 * @throws IOException if the template file could not be retrieved
	 */
	protected Template getTemplate(String name, Locale locale) throws IOException {
		return (getEncoding() != null ?
				getConfiguration().getTemplate(name, locale, getEncoding()) :
				getConfiguration().getTemplate(name, locale));
	}
	
	/**
	 * Check that the FreeMarker template used for this view exists and is valid.
	 * <p>Can be overridden to customize the behavior, for example in case of
	 * multiple templates to be rendered into a single view.
	 */
	@Override
	public boolean checkResource(Locale locale) throws Exception {
		try {
			// Check that we can get the template, even if we might subsequently get it again.
			getConfiguration().getTemplate(getUrl());
			return true;
		}
		catch (FileNotFoundException ex) {
			return false;
		}
		catch (ParseException ex) {
			throw new ApplicationContextException(
					"Failed to parse FreeMarker template for URL [" +  getUrl() + "]", ex);
		}
		catch (IOException ex) {
			throw new ApplicationContextException(
					"Could not load FreeMarker template for URL [" + getUrl() + "]", ex);
		}
	}
}