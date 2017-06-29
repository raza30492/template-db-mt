package com.jazasoft.mt.restcontroller;

import com.jazasoft.mt.ApiUrls;
import com.jazasoft.mt.entity.master.UrlInterceptor;
import com.jazasoft.mt.service.InterceptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mdzahidraza on 28/06/17.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_INTERCEPTORS)
public class InterceptorRestController {

    private InterceptorService interceptorService;

    @Autowired
    public InterceptorRestController(InterceptorService interceptorService) {
        this.interceptorService = interceptorService;
    }

    @GetMapping
    public ResponseEntity<?> getUrlInterceptors(){
        List<UrlInterceptor> interceptors = interceptorService.findAll();
        return ResponseEntity.ok(interceptors);
    }


    @GetMapping(ApiUrls.URL_INTERCEPTORS_INTERCEPTOR)
    public ResponseEntity<?> getUrlInterceptor(@PathVariable("interceptorId") Long interceptorId){
        UrlInterceptor interceptor = interceptorService.findOne(interceptorId);
        if (interceptor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interceptor);
    }

    @PostMapping
    public ResponseEntity<?> saveUrlInterceptor(@RequestBody UrlInterceptor interceptor){
        interceptor = interceptorService.save(interceptor);
        return ResponseEntity.ok(interceptor);
    }
}
