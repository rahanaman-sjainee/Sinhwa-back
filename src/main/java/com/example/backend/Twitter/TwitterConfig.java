package com.example.backend.Twitter;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.web.bind.annotation.PostMapping;


@Configuration

public class TwitterConfig {

    @Value("${twitter.consumer-key}")
    private String consumerKey;

    @Value("${twitter.consumer-key-secret}")
    private String consumerKeySecret;




    @Bean
    public OAuth1Operations twitterAuth() {
        TwitterConnectionFactory connectionFactory =
                new TwitterConnectionFactory(consumerKey,consumerKeySecret);
        return connectionFactory.getOAuthOperations();
    }

    @PostConstruct
    public void init() {
        System.out.println(consumerKey);
        System.out.println(consumerKeySecret);
    }







//    @Bean
//    public Twitter twitter() {
//        return new TwitterTemplate(consumerKey, consumerKeySecret);
//    }
//
//    @Bean
//    public TwitterConnectionFactory twitterConnectionFactory() {
//        return new TwitterConnectionFactory(consumerKey, consumerKeySecret);
//    }
}
