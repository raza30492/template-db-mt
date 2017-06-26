package com.jazasoft.mt.service;

import com.jazasoft.mt.entity.master.Company;
import com.jazasoft.mt.repository.master.CompanyRepository;
import com.jazasoft.mt.tenant.MultiTenantConnectionProviderImpl;
import com.jazasoft.mt.util.ApplicationContextUtil;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager", readOnly = true)
public class CompanyService {
    private final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

    private CompanyRepository companyRepository;

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

    @Transactional
    public Company save(Company company) {
        LOGGER.debug("save: company = {}", company);
        MultiTenantConnectionProviderImpl multiTenantConnectionProvider = ApplicationContextUtil.getApplicationContext().getBean(MultiTenantConnectionProviderImpl.class);
        multiTenantConnectionProvider.addDatasource(company.getDbName());
        return companyRepository.save(company);
    }
    
    public long count() {
        return companyRepository.count();
    }
}
