package com.jazasoft.mt.service;

import com.jazasoft.mt.MyEvent;
import com.jazasoft.mt.entity.master.Company;
import com.jazasoft.mt.repository.master.CompanyRepository;
import com.jazasoft.mt.tenant.MultiTenantConnectionProviderImpl;
import com.jazasoft.mt.util.ApplicationContextUtil;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class CompanyService implements ApplicationEventPublisherAware {
    private final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

    private CompanyRepository companyRepository;

    private ApplicationEventPublisher publisher;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company findOne(Long id){
        LOGGER.debug("findOne: id = {}", id);
        return companyRepository.findOne(id);
    }

    public List<Company> findAll(){
        LOGGER.debug("findAll");
        return companyRepository.findAll();
    }

    public Company save(Company company) {
        LOGGER.debug("save: company = {}", company);
        publisher.publishEvent(new MyEvent(applicationContext,company.getDbName()));
        return companyRepository.save(company);
    }
    
    public long count() {
        return companyRepository.count();
    }

    public boolean exists(Long id) {
        return companyRepository.exists(id);
    }
}
