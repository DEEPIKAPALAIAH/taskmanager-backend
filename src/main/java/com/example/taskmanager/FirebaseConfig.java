package com.example.taskmanager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws Exception {

        FileInputStream serviceAccount =
            new FileInputStream("src/main/resources/serviceAccountKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://task-manager-app-77e2f-default-rtdb.firebaseio.com/")
            .build();

        FirebaseApp.initializeApp(options);

        System.out.println("Firebase Connected Successfully");
    }
}