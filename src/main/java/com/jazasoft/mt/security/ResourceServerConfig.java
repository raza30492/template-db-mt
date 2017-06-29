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

//@Configuration
//@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

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
                .authorizeRequests().antMatchers("/**").authenticated();
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
     * Instantiates Bean accessDecisionManager, instance of {@link UnanimousBased} with {@link ScopeVoter}, {@link RoleVoter}
     * and {@link AuthenticatedVoter}.
     *
     * @return {@link AccessDecisionManager} bean, instance of {@link UnanimousBased}
     */
    @Bean
    public AccessDecisionManager accessDecisionManager() {
//        RoleVoter roleVoter = new RoleVoter(){
//            @Override
//            public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
//                //System.out.println("username = "+((Principal)authentication.getPrincipal()).getName());
//                authentication.getAuthorities().forEach(authorities -> {
//                    System.out.println("auths: " + authorities.getAuthority());
//                });
//                attributes.forEach(attribute -> {
//                    System.out.println("attribute = "+attribute.getAttribute() );
//                });
//                int vote = super.vote(authentication, object, attributes);
//                System.out.println("vote: " + vote);
//                return vote;
//            }
//        };
//        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter(){
//            @Override
//            public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
//                //System.out.println("username = "+((Principal)authentication.getPrincipal()).getName());
//                authentication.getAuthorities().forEach(authorities -> {
//                    System.out.println("auths2: " + authorities.getAuthority());
//                });
//                attributes.forEach(attribute -> {
//                    System.out.println("attribute2 = "+attribute.getAttribute() );
//                });
//                int vote = super.vote(authentication, object, attributes);
//                System.out.println("vote2: " + vote);
//                return vote;
//            }
//        };
        return new AffirmativeBased(Arrays.asList(new ScopeVoter(),new RoleVoter(),new AuthenticatedVoter()));
    }

//    @Autowired
//    TokenStore tokenStore;
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.resourceId("resource").tokenStore(tokenStore);
//    }

//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//                .requestMatchers().antMatchers("/api/**")
//            .and()
//                .authorizeRequests()
////                    .antMatchers("/api/users","/api/users/**").access("hasRole('ADMIN')")
////                    .antMatchers("/api/**").authenticated()
//                    .antMatchers("/api/**").permitAll()
//            .and()
//                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
//
//    }
}



//        http.
//        anonymous().disable()
//        .requestMatchers().antMatchers("/**")
//        .and().authorizeRequests()
//        .antMatchers("/**").access("hasRole('ADMIN')")
//        .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
// @formatter:off
//        http
//                // Since we want the protected resources to be accessible in the UI as well we need 
//                // session creation to be allowed (it's disabled by default in 2.0.6)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and()
//                .requestMatchers().antMatchers("/photos/**", "/oauth/users/**", "/oauth/clients/**", "/me")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/me").access("#oauth2.hasScope('read')")
//                .antMatchers("/photos").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
//                .antMatchers("/photos/trusted/**").access("#oauth2.hasScope('trust')")
//                .antMatchers("/photos/user/**").access("#oauth2.hasScope('trust')")
//                .antMatchers("/photos/**").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
//                .regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
//                .access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('write')")
//                .regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
//                .access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
//                .regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
//                .access("#oauth2.clientHasRole('ROLE_CLIENT') and #oauth2.isClient() and #oauth2.hasScope('read')");
        // @formatter:on
