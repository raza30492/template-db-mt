package com.jazasoft.mt.restcontroller;

/**
 * Created by mdzahidraza on 23/06/17.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jazasoft.mt.ApiUrls;
import com.jazasoft.mt.dto.OauthResponse;
import com.jazasoft.mt.entity.master.Company;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Ignore
public class CompanyIntegrationTest {

    @Autowired  MockMvc mvc;

    @Autowired ObjectMapper mapper;

    private final String contentType = "application/hal+json;charset=UTF-8";

    private String accessToken1 = "";
    private String accessToken2 = "";


    @Before
    public void setUp() throws Exception{
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","password");
        params.add("username","zahid7292");
        params.add("password","admin");
        MvcResult mvcResult = mvc.perform(post(ApiUrls.OAUTH_URL)
                .params(params)
                .header("Authorization","Basic Y2xpZW50OnNlY3JldA==")
        )
                .andExpect(status().isOk())
                .andReturn();
        String resp = mvcResult.getResponse().getContentAsString();
        System.out.println(resp);
        OauthResponse oauthResponse = mapper.readValue(resp, OauthResponse.class);
        accessToken1 = oauthResponse.getAccess_token();

        params.clear();
        params.add("grant_type","password");
        params.add("username","taufeeque8");
        params.add("password","admin");
        mvcResult = mvc
                .perform(post(ApiUrls.OAUTH_URL)
                        .params(params)
                        .header("Authorization","Basic Y2xpZW50OnNlY3JldA==")
                )
                .andExpect(status().isOk())
                .andReturn();
        resp = mvcResult.getResponse().getContentAsString();
        System.out.println(resp);
        oauthResponse = mapper.readValue(resp, OauthResponse.class);
        accessToken2 = oauthResponse.getAccess_token();
    }

    @Test
    public void getAllCompany() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_COMPANIES).header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.companys",hasSize(3)))
                .andExpect(jsonPath("$._embedded.companys[0].name",is("Md Zahid Raza")))
                .andExpect(jsonPath("$._embedded.companys[0]._links.self").exists());

        this.mvc.perform(get(ApiUrls.ROOT_URL_COMPANIES).header("Authorization","Bearer " + accessToken2))
                .andExpect(status().isForbidden());
    }


    @Test
    public void getCompany() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_COMPANIES +"/1").header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Md Zahid Raza")))
                .andExpect(jsonPath("$._links.self").exists());

        this.mvc.perform(get(ApiUrls.ROOT_URL_COMPANIES +"/10").header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isNotFound());
    }


    @Test
    public void createCompany() throws Exception{
        Company company = new Company("Test Company","test company description", "Test Address","tnt_db_test");
        System.out.println("-$$$-" +mapper.writeValueAsString(company));
        MvcResult mvcResult = mvc.perform(post(ApiUrls.ROOT_URL_COMPANIES)
                .content(mapper.writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isCreated())
                .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains(ApiUrls.ROOT_URL_COMPANIES));

        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx+1);

        this.mvc.perform(get( ApiUrls.ROOT_URL_COMPANIES +"/{id}",id).header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test Company")));
        
    }

    @Test
    public void createCompanyBadRequest() throws Exception{
        Company company = new Company();
        this.mvc.perform(post(ApiUrls.ROOT_URL_COMPANIES)
                .content(mapper.writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(4)));

        //Test each fields one by one
        company = new Company("","test_company", "test@gmail.com", "8987525008");
        this.mvc.perform(post(ApiUrls.ROOT_URL_COMPANIES)
                .content(mapper.writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", containsString("length must be between 3")));

        company = new Company("Md Zahid Raza","test company", "test@gmail.com", "8987525008");
        this.mvc.perform(post(ApiUrls.ROOT_URL_COMPANIES)
                .content(mapper.writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("username")));

        company = new Company("Md Zahid Raza","test_company", "test", "8987525008");
        this.mvc.perform(post(ApiUrls.ROOT_URL_COMPANIES)
                .content(mapper.writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("email")))
                .andExpect(jsonPath("$[0].message", containsString("Incorrect email")));


        company = new Company("Md Zahid Raza","test_company", "test@gmail.com", "8987525");
        this.mvc.perform(post(ApiUrls.ROOT_URL_COMPANIES)
                .content(mapper.writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("mobile")))
                .andExpect(jsonPath("$[0].message", containsString("mobile number should be 10 digit long")));

    }

}

