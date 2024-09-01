package com.example.backend.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

//firebase 연동
@Configuration
public class FirebaseConfig {

    @Value("${firebase.firebase-config-file}")
    private String firebaseConfigPath;

    @Value("${firebase.firebase-bucket}")
    private String firebaseBucket;

    @PostConstruct
    public void init( ) {
        System.out.println(firebaseConfigPath);
        try {

            FileInputStream refreshToken = new FileInputStream(firebaseConfigPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setDatabaseUrl("https://Sinhwa.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}