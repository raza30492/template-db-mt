package com.jazasoft.mt.tenant;



import com.jazasoft.mt.Constants;
import com.jazasoft.mt.entity.master.User;
import com.jazasoft.mt.repository.master.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.Principal;
import java.util.Optional;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {
        Principal principal = req.getUserPrincipal();
        boolean tenantSet = false;
        if (principal != null) {
            User user = null;
            Optional<User> uo = userRepository.findOneByEmail(principal.getName());
            if (uo.isPresent()){
                user = uo.get();
            }else {
                uo = userRepository.findOneByUsername(principal.getName());
                if (uo.isPresent()) {
                    user = uo.get();
                }
            }
            if (user != null && user.getCompany() != null) {
                req.setAttribute(Constants.CURRENT_TENANT_IDENTIFIER, user.getCompany().getDbName());
                tenantSet = true;
            }else {
                req.setAttribute(Constants.CURRENT_TENANT_IDENTIFIER, Constants.UNKNOWN_TENANT);
                tenantSet = true;
            }
        }
//
//        String tenant = req.getHeader(TENANT_HEADER);
//        boolean tenantSet = false;
//
//        if(StringUtils.isEmpty(tenant)) {
//            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            res.getWriter().write("{\"error\": \"No tenant supplied\"}");
//            res.getWriter().flush();
//        } else {
//            TenantContext.setCurrentTenant(tenant);
//            tenantSet = true;
//        }
//
        return tenantSet;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }
}


