package com.jazasoft.mt.restcontroller;

/**
 * Created by mdzahidraza on 23/06/17.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jazasoft.mt.ApiUrls;
import com.jazasoft.mt.dto.OauthResponse;
import com.jazasoft.mt.dto.UserDto;
import org.junit.Before;
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
//@Ignore
public class UserIntegrationTest {

    @Autowired  MockMvc mvc;

    @Autowired ObjectMapper mapper;

    private final String contentType = "application/hal+json;charset=UTF-8";

    private String accessToken = "";


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
        OauthResponse oauthResponse = mapper.readValue(resp, OauthResponse.class);
        accessToken = oauthResponse.getAccess_token();
    }

    @Test
    public void getAllUser() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_USERS).header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.users",hasSize(3)))
                .andExpect(jsonPath("$._embedded.users[0].name",is("Md Zahid Raza")))
                .andExpect(jsonPath("$._embedded.users[0]._links.self").exists());
    }


    @Test
    public void getUser() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_USERS +"/1").header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Md Zahid Raza")))
                .andExpect(jsonPath("$._links.self").exists());

        this.mvc.perform(get(ApiUrls.ROOT_URL_USERS +"/10").header("Authorization","Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }


    @Test
    public void createAndDeleteUser() throws Exception{
        UserDto user = new UserDto("Test UserDto","test_user", "test@gmail.com","8987525008");
        System.out.println("-$$$-" +mapper.writeValueAsString(user));
        MvcResult mvcResult = mvc.perform(post(ApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken)
        )
                .andExpect(status().isCreated())
                .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains(ApiUrls.ROOT_URL_USERS));

        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx+1);

        this.mvc.perform(get( ApiUrls.ROOT_URL_USERS +"/{id}",id).header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test UserDto")));

        this.mvc.perform(delete(ApiUrls.ROOT_URL_USERS + "/{id}", id).header("Authorization","Bearer " + accessToken))
                .andExpect(status().isNoContent());

        this.mvc.perform(get(ApiUrls.ROOT_URL_USERS + "/{id}", id).header("Authorization","Bearer " + accessToken))
                .andExpect(jsonPath("$.enabled", is(false)));
    }

    @Test
    public void createUserBadRequest() throws Exception{
        UserDto user = new UserDto();
        this.mvc.perform(post(ApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(4)));

        //Test each fields one by one
        user = new UserDto("","test_user", "test@gmail.com", "8987525008");
        this.mvc.perform(post(ApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", containsString("length must be between 3")));

        user = new UserDto("Md Zahid Raza","test user", "test@gmail.com", "8987525008");
        this.mvc.perform(post(ApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("username")));

        user = new UserDto("Md Zahid Raza","test_user", "test", "8987525008");
        this.mvc.perform(post(ApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("email")))
                .andExpect(jsonPath("$[0].message", containsString("Incorrect email")));


        user = new UserDto("Md Zahid Raza","test_user", "test@gmail.com", "8987525");
        this.mvc.perform(post(ApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("mobile")))
                .andExpect(jsonPath("$[0].message", containsString("mobile number should be 10 digit long")));

    }

}

