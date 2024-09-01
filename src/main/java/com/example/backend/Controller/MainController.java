package com.example.backend.Controller;


import com.example.backend.Auth.AuthFailException;
import com.example.backend.Auth.UserProfile;
import com.example.backend.Firebase.FirebaseService;
import com.example.backend.Auth.Twitter.TwitterService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String aa(HttpServletResponse response) {

        //template 속 html 로 리다이렉트
        return "test";

    }
    @GetMapping("/test")
    public void test(HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie("test", "test");
//        cookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
//        cookie.setSecure(true); // HTTPS를 사용하는 경우 Secure 플래그 설정
//        cookie.setPath("/");
        cookie.setMaxAge(3600); // 쿠키의 유효 기간 설정 (예: 1시간)
        response.addCookie(cookie);
        response.sendRedirect("/");
    }

    @GetMapping("/api")
    public ResponseEntity<Map<String, Object>> index() {

        Map<String, Object> response = new HashMap<>();
        response.put("auth", "aaa");
        return ResponseEntity.ok(response);



    }

    @GetMapping("api/login-token")
    public ResponseEntity<String> getLoginToken(HttpServletRequest request){
        String name ="idToken";
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("idToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not found");
        }
        return ResponseEntity.ok(token);
    }


    @GetMapping("api/login/twittr")
    public ResponseEntity<Void> loginTwitter(HttpServletResponse response) throws IOException {
//        ResponseEntity<String> res = new RestTemplate().getForEntity(twitterService.getRequestURL(), String.class);
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .header(HttpHeaders.LOCATION, twitterService.getRequestURL())
//                .build();
//        return response.sendRedirect(twitterService.getRequestURL());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(twitterService.getRequestURL()));
        //return response;
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("api/login/twitter")
    @ResponseBody
    public String twittertest(){
        return twitterService.getRequestURL();
    }

    @GetMapping("api/login/twitter/process")
    @ResponseBody
    public void twitterprocess(@RequestParam(value = "oauth_token",required = false) String oauthToken, @RequestParam(value = "oauth_verifier",required = false) String oauthVerifier, HttpServletResponse response) throws AuthFailException, IOException {
        if(oauthToken == null || oauthVerifier == null){throw new AuthFailException();}
        OAuthToken accessToken = twitterAuth.exchangeForAccessToken(
                new AuthorizedRequestToken(new OAuthToken(oauthToken, ""), oauthVerifier), null);
        Twitter twitter = new TwitterTemplate(consumerKey, consumerKeySecret, accessToken.getValue(), accessToken.getSecret());
        TwitterProfile profile = twitter.userOperations().getUserProfile();
        Cookie cookie = new Cookie("idToken", twitterService.getUserID(profile));
        cookie.setHttpOnly(true); // XSS 공격 방지를 위해 HttpOnly 설정
//        cookie.setSecure(true); // HTTPS를 사용하는 경우 Secure 플래그 설정
        cookie.setPath("/api");
        cookie.setMaxAge(3600); // 쿠키의 유효 기간 설정 (예: 1시간)
        response.addCookie(cookie);

        // 프론트엔드 애플리케이션으로 리다이렉트
       response.sendRedirect("http://192.168.219.101:3000/login");// 로그인 성공 페이지


    }

//    public ResponseEntity<Map<String, Object>> twitterprocess(@RequestParam(value = "oauth_token",required = false) String oauthToken, @RequestParam(value = "oauth_verifier",required = false) String oauthVerifier) throws AuthFailException, IOException {
//        if(oauthToken == null || oauthVerifier == null){throw new AuthFailException();}
//        OAuthToken accessToken = twitterAuth.exchangeForAccessToken(
//                new AuthorizedRequestToken(new OAuthToken(oauthToken, ""), oauthVerifier), null);
//        Twitter twitter = new TwitterTemplate(consumerKey, consumerKeySecret, accessToken.getValue(), accessToken.getSecret());
//        TwitterProfile profile = twitter.userOperations().getUserProfile();
//        String id = twitterService.getUserID(profile);
//        System.out.println(id);
//        Map<String, Object> response = new HashMap<>();
//        response.put("isSuccess", false);
//        response.put("id", id);
//        response.put("name", profile.getName());
//        return ResponseEntity.ok(response);
//        //response.sendRedirect("/");
//    }

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
