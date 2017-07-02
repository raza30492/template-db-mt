package com.jazasoft.mt.security;

import com.jazasoft.mt.util.YamlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
    private static String REALM="MY_OAUTH_REALM";
    private static final String ENV_OAUTH2 = "security.oauth2.client.";
 
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired TokenStore tokenStore;
 
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        List clientList = (List) YamlUtils.getINSTANCE().getAppProperty("security.oauth2.clients");

        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        for (int i = 0; i < clientList.size(); i++) {
            Map client = (Map) clientList.get(i);
            builder
                    .withClient((String) client.get("id"))
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                    .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                    .scopes((String) client.get("scope"))
                    .secret((String) client.get("secret"))
                    .accessTokenValiditySeconds(Integer.parseInt((String) (String) client.get("access-token-validity")))
                    .refreshTokenValiditySeconds(Integer.parseInt((String) (String) client.get("refresh-token-validity")));
        }
    }
 
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager);
    }
 
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.realm(REALM+"/client");
    }

    @Bean
    public TokenStore tokenStore(DataSource dataSource) {
        return new JdbcTokenStore(dataSource);
    }
}
