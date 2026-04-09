package com.example.taskmanager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws Exception {

        String projectId = System.getenv("FIREBASE_PROJECT_ID");
        String clientEmail = System.getenv("FIREBASE_CLIENT_EMAIL");
        String privateKey = System.getenv("FIREBASE_PRIVATE_KEY").replace("\\n", "\n");

        String json = String.format(
            "{ \"type\": \"service_account\", \"project_id\": \"%s\", \"private_key_id\": \"dummy\", \"private_key\": \"%s\", \"client_email\": \"%s\", \"client_id\": \"dummy\", \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\", \"token_uri\": \"https://oauth2.googleapis.com/token\", \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\", \"client_x509_cert_url\": \"dummy\" }",
            projectId, privateKey, clientEmail
        );

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))
            ))
            .setDatabaseUrl("https://task-manager-app-77e2f-default-rtdb.firebaseio.com/")
            .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        System.out.println("Firebase Connected Successfully");
    }
}