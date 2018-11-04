package com.tmt.common.security.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectView {

	public static final String DEFAULT_ENCODING_SCHEME = "UTF-8";

    private String url;

    private boolean contextRelative = false;

    private boolean http10Compatible = true;

    private String encodingScheme = DEFAULT_ENCODING_SCHEME;

    public RedirectView() {}

    public RedirectView(String url) {
        setUrl(url);
    }

    public RedirectView(String url, boolean contextRelative) {
        this(url);
        this.contextRelative = contextRelative;
    }

    public RedirectView(String url, boolean contextRelative, boolean http10Compatible) {
        this(url);
        this.contextRelative = contextRelative;
        this.http10Compatible = http10Compatible;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    @SuppressWarnings("rawtypes")
	public final void renderMergedOutputModel(
            Map model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        StringBuilder targetUrl = new StringBuilder();
        if (this.contextRelative && getUrl().startsWith("/")) {
            targetUrl.append(request.getContextPath());
        }
        targetUrl.append(getUrl());
        appendQueryProperties(targetUrl, model, this.encodingScheme);
        sendRedirect(request, response, targetUrl.toString(), this.http10Compatible);
    }

    @SuppressWarnings("rawtypes")
    protected void appendQueryProperties(StringBuilder targetUrl, Map model, String encodingScheme)
            throws UnsupportedEncodingException {

        // Extract anchor fragment, if any.
        // The following code does not use JDK 1.4's StringBuffer.indexOf(String)
        // method to retain JDK 1.3 compatibility.
        String fragment = null;
        int anchorIndex = targetUrl.toString().indexOf('#');
        if (anchorIndex > -1) {
            fragment = targetUrl.substring(anchorIndex);
            targetUrl.delete(anchorIndex, targetUrl.length());
        }

        // If there aren't already some parameters, we need a "?".
        boolean first = (getUrl().indexOf('?') < 0);
        Map queryProps = queryProperties(model);

        if (queryProps != null) {
            for (Object o : queryProps.entrySet()) {
                if (first) {
                    targetUrl.append('?');
                    first = false;
                } else {
                    targetUrl.append('&');
                }
                Map.Entry entry = (Map.Entry) o;
                String encodedKey = urlEncode(entry.getKey().toString(), encodingScheme);
                String encodedValue =
                        (entry.getValue() != null ? urlEncode(entry.getValue().toString(), encodingScheme) : "");
                targetUrl.append(encodedKey).append('=').append(encodedValue);
            }
        }

        // Append anchor fragment, if any, to end of URL.
        if (fragment != null) {
            targetUrl.append(fragment);
        }
    }

    protected String urlEncode(String input, String encodingScheme) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, encodingScheme);
    }

    @SuppressWarnings("rawtypes")
    protected Map queryProperties(Map model) {
        return model;
    }

    protected void sendRedirect(HttpServletRequest request, HttpServletResponse response,
                                String targetUrl, boolean http10Compatible) throws IOException {
        if (http10Compatible) {
            response.sendRedirect(response.encodeRedirectURL(targetUrl));
        } else {
            response.setStatus(303);
            response.setHeader("Location", response.encodeRedirectURL(targetUrl));
        }
    }
}
