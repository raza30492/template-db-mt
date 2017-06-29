package com.jazasoft.mt.service;

import com.jazasoft.mt.entity.master.UrlInterceptor;
import com.jazasoft.mt.repository.master.UrlInterceptorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mdzahidraza on 28/06/17.
 */
@Service
@Transactional
public class InterceptorService {

    private final Logger LOGGER = LoggerFactory.getLogger(InterceptorService.class);

    private UrlInterceptorRepository urlInterceptorRepository;

    @Autowired
    public InterceptorService(UrlInterceptorRepository urlInterceptorRepository) {
        this.urlInterceptorRepository = urlInterceptorRepository;
    }

    public UrlInterceptor findOne(Long id){
        LOGGER.debug("findOne: id = {}", id);
        return urlInterceptorRepository.findOne(id);
    }

    public List<UrlInterceptor> findAllByUrl(String url) {
        return urlInterceptorRepository.findByUrl(url);
    }

    public List<UrlInterceptor> findAllByUrlContaining(String interceptor) {
        return urlInterceptorRepository.findByUrlContaining(interceptor);
    }

    public List<UrlInterceptor> findAll(){
        LOGGER.debug("findAll");
        return urlInterceptorRepository.findAll();
    }

    public UrlInterceptor save(UrlInterceptor interceptor) {
        LOGGER.debug("save: interceptor = {}", interceptor);
        return urlInterceptorRepository.save(interceptor);
    }

    public long count() {
        return urlInterceptorRepository.count();
    }
}
