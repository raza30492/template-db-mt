package com.jazasoft.mt.security;

import com.jazasoft.mt.entity.master.UrlInterceptor;
import com.jazasoft.mt.service.InterceptorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mdzahidraza on 28/06/17.
 */
public class DynamicFilterInvocationSecurityMetadataSource extends DefaultFilterInvocationSecurityMetadataSource {

    private final Logger LOGGER = LoggerFactory.getLogger(DynamicFilterInvocationSecurityMetadataSource.class);

    private InterceptorService interceptorService;

    public DynamicFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
        super(requestMap);
    }

    public void setInterceptorService(InterceptorService urlInterceptorRepository) {
        this.interceptorService = urlInterceptorRepository;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        String httpMethod = fi.getRequest().getMethod();

        if (url != null) {
            url = url.split("\\?")[0];
            String[] urls = url.split("/");
            Pattern pattern = Pattern.compile("\\d+");
            for (int i = 0; i < urls.length; i++) {
                if (pattern.matcher(urls[i]).matches()) {
                    urls[i] = "{\\\\d+}";
                }
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < urls.length ; i++) {
                builder.append("/").append(urls[i]);
            }
            url = builder.toString();
            LOGGER.info("-$$$- Url = {}, method = {}", url, httpMethod);
            List<UrlInterceptor> interceptors = this.interceptorService.findAllByUrl(url);

            Collection<ConfigAttribute> configAttributes = interceptors.stream()
                    //If the httpMethod is null is because it is valid for all methods
                    .filter(in -> in.getHttpMethod() == null || in.getHttpMethod().equals(httpMethod))
                    .map(in -> new DynamicConfigAttribute(in.getAccess()))
                    .collect(Collectors.toList());
            if (configAttributes.isEmpty()) {
                configAttributes.add(new DynamicConfigAttribute("ROLE_UNKNOWN"));
            }
            configAttributes.forEach(configAttribute -> System.out.println("-$$$- " + configAttribute.getAttribute()));
            return configAttributes;
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


    /**
     * {@link ConfigAttribute} with specific attribute (access rule).
     * Possible values to getAttribute's return:
     *  - IS_AUTHENTICATED_ANONYMOUSLY - No token in the request
     *  - IS_AUTHENTICATED_REMEMBERED
     *  - IS_AUTHENTICATED_FULLY - With a valid token
     *  - SCOPE_<scope> - Token with a specific scope
     *  - ROLE_<role> - Token's user with specific role
     * @author mariane.vieira
     *
     */
    public class DynamicConfigAttribute implements ConfigAttribute {
        private static final long serialVersionUID = 1201502296417220314L;
        private String attribute;
        public DynamicConfigAttribute(String attribute) {
            this.attribute = attribute;
        }
        @Override
        public String getAttribute() {
            /* Possible values to getAttribute's return:
             * IS_AUTHENTICATED_ANONYMOUSLY, IS_AUTHENTICATED_REMEMBERED
             * IS_AUTHENTICATED_FULLY, SCOPE_<scope>, ROLE_<role>
             */
            return this.attribute;
        }
        @Override
        public String toString() {
            return this.attribute;
        }
    }
}