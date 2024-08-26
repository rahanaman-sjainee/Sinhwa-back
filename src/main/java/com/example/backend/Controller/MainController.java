package com.example.backend.Controller;


import com.example.backend.Firebase.FirebaseService;
import com.example.backend.Twitter.TwitterConfig;
import com.example.backend.Twitter.TwitterService;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private final FirebaseService firebaseService;

    @Autowired
    private final TwitterService twitterService;
    @Value("${twitter.consumer-key}")
    private String consumerKey;

    @Value("${twitter.consumer-key-secret}")
    private String consumerKeySecret;

    @Value("${twitter.callbackUrl}")
    private String callbackURL;

    @Autowired
    private final OAuth1Operations twitterAuth;

    @GetMapping("/")
    public String index() {

        //template 속 html 로 리다이렉트
        return "test";

    }
    @GetMapping("/login/twitter")
    public void twittertest(HttpServletResponse response) throws IOException {

        TwitterConnectionFactory connectionFactory =
                new TwitterConnectionFactory(consumerKey,consumerKeySecret);
        OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuthToken requestToken = twitterAuth.fetchRequestToken(callbackURL, null);
        System.out.println(requestToken);

        String token = requestToken.getValue();
        String authorizeUrl  = oauthOperations.buildAuthorizeUrl(token, OAuth1Parameters.NONE);
        System.out.println(authorizeUrl);
        response.sendRedirect(authorizeUrl );
    }

    @GetMapping("login/twitter/process")
    public String twitterprocess(HttpServletRequest request, HttpServletResponse response){


        request.getParameter("denied");
        if(request.getParameter("denied")==null){
            System.out.println("go");
        }
        return "test";
    }

//    public String twittertest(@RequestBody Map<String, Object> codeMap){
//        TwitterConnectionFactory connectionFactory =
//                new TwitterConnectionFactory(consumerKey,consumerKeySecret);
//        OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
//        String oauthToken = String.valueOf(codeMap.get("oauthToken"));
//        String oauthVerifier = String.valueOf(codeMap.get("oauthVerifier"));
//        OAuthToken accessToken = oauthOperations.exchangeForAccessToken(
//                new AuthorizedRequestToken(new OAuthToken(oauthToken, ""), oauthVerifier), null);
//        System.out.println("Token Value:- accesstoken");
//        Twitter twitter = new TwitterTemplate(consumerKey,
//                consumerKeySecret,
//                accessToken.getValue(),
//                accessToken.getSecret());
//        TwitterProfile profile = twitter.userOperations().getUserProfile();
//        profile.getId();
//        System.out.println(profile.getName());
//        return "test";
//    }

    //@PostMapping 도 가능
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String post(@RequestParam("files") List<MultipartFile> files) throws IOException{
        for (MultipartFile file : files) {
            firebaseService.uploadFiles(file,"test/",file.getName());
        }
//        files.forEach(file ->{
//            if(file.isEmpty()) return;
//            try {
//                firebaseService.uploadFiles(file,file.getName());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
        return "test";
    }

}
