package com.example.backend.Twitter;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterObjectFactory;



@Configuration
public class TwitterConfig {
    @Value("${twitter.consumer-key")
    private String consumerKey;

    @Value("${twitter.consumer-key-secret")
    private String consumerKeySecret;

    @PostConstruct
    public void init() {
        Twitter twitter = new TwitterObjectFactory().getInstance();
    }
}
