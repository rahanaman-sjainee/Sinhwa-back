package com.example.backend.Twitter;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Service;


@Service
public class TwitterService {
    @Value("${twitter.consumer-key")
    private String consumerKey;

    @Value("${twitter.consumer-key-secret")
    private String consumerKeySecret;


    @Autowired
    private final OAuth1Operations twitterAuth;


    private String callbackUrl = "http://localhost:8080/social/twitter";

    public TwitterService(OAuth1Operations twitterAuth) {
        this.twitterAuth = twitterAuth;
    }

    public String getTwitterUrl() {
        TwitterConnectionFactory connectionFactory =
                new TwitterConnectionFactory(consumerKey,consumerKeySecret);
        OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuthToken requestToken = oauthOperations.fetchRequestToken(callbackUrl, null);
        String token = requestToken.getValue();
        return oauthOperations.buildAuthorizeUrl(token, OAuth1Parameters.NONE);
    }
}
