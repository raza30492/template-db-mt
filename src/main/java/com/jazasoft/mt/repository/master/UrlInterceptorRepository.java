package com.jazasoft.mt.repository.master;

import com.jazasoft.mt.entity.master.Company;
import com.jazasoft.mt.entity.master.UrlInterceptor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mdzahidraza on 28/06/17.
 */
public interface UrlInterceptorRepository extends JpaRepository<UrlInterceptor,Long> {

    List<UrlInterceptor> findByUrl(String url);

    List<UrlInterceptor> findByCompanyAndUrl(Company company, String url);

    List<UrlInterceptor> findByUrlContaining(String url);
}
