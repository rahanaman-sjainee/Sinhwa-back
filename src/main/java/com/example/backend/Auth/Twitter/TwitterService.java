package com.example.backend.Auth.Twitter;

import com.example.backend.Auth.AuthFailException;
import com.example.backend.Auth.AuthService;
import com.example.backend.Auth.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Service;


@Service
public class TwitterService implements AuthService {
    @Value("${twitter.consumer-key")
    private String consumerKey;

    @Value("${twitter.consumer-key-secret")
    private String consumerKeySecret;


    @Autowired
    private final OAuth1Operations twitterAuth;

    @Value("${twitter.callbackUrl}")
    private String callbackURL;

    public TwitterService(OAuth1Operations twitterAuth) {
        this.twitterAuth = twitterAuth;
    }


    public String getUserID(TwitterProfile profile) {
        return "twitter"+profile.getId();
    }


    @Override
    public String getRequestURL() {
        TwitterConnectionFactory connectionFactory =
                new TwitterConnectionFactory(consumerKey,consumerKeySecret);
        OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuthToken requestToken = twitterAuth.fetchRequestToken(callbackURL, null);
//        System.out.println(requestToken);
        String token = requestToken.getValue();
        String authorizeUrl  = oauthOperations.buildAuthorizeUrl(token, OAuth1Parameters.NONE);
//        System.out.println(authorizeUrl);
        return oauthOperations.buildAuthorizeUrl(token, OAuth1Parameters.NONE);
    }
}
