package com.example.taskmanager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws Exception {

        // If already initialized, return existing instance
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        // Load the service account key from resources
        InputStream serviceAccount = getClass()
                .getClassLoader()
                .getResourceAsStream("serviceAccountKey.json");

        if (serviceAccount == null) {
            throw new RuntimeException("serviceAccountKey.json NOT FOUND in src/main/resources");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build(); // Do NOT set databaseUrl unless you use Realtime DB

        return FirebaseApp.initializeApp(options);
    }
}