package com.redis.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Configuration
public class RedisTest {
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("redisTest")
    public String redisTest(){
        User user=new User();
        redisUtil.remove("user");
        user.setName("zhang");
        if(redisUtil.exists("user")){
            System.out.println(111);
        }else{
            redisUtil.set("user", user);
            User user1=(User)redisUtil.get("user");
            System.out.println(user1.getName());
        }
        return "hello World!";
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SpringApplication.run(RedisTest.class, args);
    }
}
