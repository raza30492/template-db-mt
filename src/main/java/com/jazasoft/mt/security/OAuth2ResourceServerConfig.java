package com.jazasoft.mt.security;

import com.jazasoft.mt.repository.master.UrlInterceptorRepository;
import com.jazasoft.mt.service.InterceptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.vote.ScopeVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private InterceptorService interceptorService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // Since we want the protected resources to be accessible in the UI as well we need
                // session creation to be allowed (it's disabled by default in 2.0.6)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .addFilter(filterSecurityInterceptor())
                //permitting all because security paths verifications are going to be dynamic
                //because of this filter above
                .authorizeRequests().antMatchers("/**").permitAll();
    }

    /**
     * Instantiates Bean remoteTokenServices filterSecurityInterceptor, instance of {@link DynamicFilterInvocationSecurityMetadataSource}
     * that intercepts every request to verify security rules. These rules are stored in database and can be formed and verified
     * dynamically.
     *
     * @return {@link FilterSecurityInterceptor} Bean, instance of {@link DynamicFilterInvocationSecurityMetadataSource}
     */
    public FilterSecurityInterceptor filterSecurityInterceptor() {

        DynamicFilterInvocationSecurityMetadataSource dynamicFilter = new DynamicFilterInvocationSecurityMetadataSource(
                new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>());
        dynamicFilter.setInterceptorService(interceptorService);
        FilterSecurityInterceptor filter = new FilterSecurityInterceptor();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAccessDecisionManager(accessDecisionManager());
        filter.setSecurityMetadataSource(dynamicFilter);
        return filter;
    }

    /**
     * Instantiates Bean accessDecisionManager, instance of {@link AffirmativeBased} with {@link ScopeVoter}, {@link RoleVoter}
     * and {@link AuthenticatedVoter}.
     *
     * @return {@link AccessDecisionManager} bean, instance of {@link UnanimousBased}
     */
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new AffirmativeBased(Arrays.asList(new ScopeVoter(),new RoleVoter(),new AuthenticatedVoter()));
    }
}

