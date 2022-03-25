package com.careerdevs.goApi.controllers;

import com.careerdevs.goApi.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private Environment env;

    @GetMapping("/all")
    public ResponseEntity getAll( RestTemplate restTemplate){
        try{
            ArrayList<UserModel> allUsers = new ArrayList<>();
            String url = "https://gorest.co.in/public/v2/users";

            ResponseEntity<UserModel[]> response = restTemplate.getForEntity(url, UserModel[].class);
            allUsers.addAll(Arrays.asList(Objects.requireNonNull(response.getBody())));

            int totalPageNumber = 4;// Integer.parseInt(response.getHeaders().get("X-Pagination-Pages").get(0));

            for(int i = 2 ; i <= totalPageNumber ; i++){
                String tempUrl = url + "?page=" + i;
                UserModel[] pageData = restTemplate.getForObject(tempUrl, UserModel[].class);
                //allUsers.addAll(Arrays.asList(pageData));
                allUsers.addAll(Arrays.asList(Objects.requireNonNull(pageData)));
            }



            return new ResponseEntity(allUsers , HttpStatus.OK);


        }
        catch(Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
