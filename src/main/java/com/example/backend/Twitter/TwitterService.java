package com.example.backend.Twitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwitterService {
    @Value("${twitter.consumer-key")
    private String consumerKey;

    @Value("${twitter.consumer-key-secret")
    private String consumerKeySecret;



}
