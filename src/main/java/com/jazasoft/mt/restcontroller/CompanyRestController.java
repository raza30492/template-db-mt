package com.jazasoft.mt.restcontroller;

import com.jazasoft.mt.ApiUrls;
import com.jazasoft.mt.entity.master.Company;
import com.jazasoft.mt.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_COMPANIES)
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<?> getCompanies() {
        List<Company> companies = companyService.findAll();
        return ResponseEntity.ok(companies);
    }

    @GetMapping(ApiUrls.URL_COMPANIES_COMPANY)
    public ResponseEntity<?> getCompany(@PathVariable("companyId") Long companyId) {
        Company company = companyService.findOne(companyId);
        if (company == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(company);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Company company){
        company = companyService.save(company);
        return ResponseEntity.ok(company);
    }
}
