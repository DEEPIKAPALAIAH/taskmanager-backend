package com.example.taskmanager;

import java.io.ByteArrayInputStream;   // ✅ ADD THIS
import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws Exception {

        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        String firebaseConfig = System.getenv("FIREBASE_KEY");

        if (firebaseConfig == null) {
            throw new RuntimeException("FIREBASE_KEY not set");
        }

        InputStream serviceAccount =
                new ByteArrayInputStream(firebaseConfig.getBytes());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://task-manager-app-77e2f-default-rtdb.firebaseio.com/")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}