package com.example.taskmanager;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    // 🔥 Firebase Init
@PostConstruct
public void init() {
    try {
        InputStream serviceAccount =
                getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

        if (serviceAccount == null) {
            System.out.println("❌ Firebase key NOT FOUND");
            throw new RuntimeException("Firebase key missing");
        } else {
            System.out.println("✅ Firebase key LOADED");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://task-manager-app-77e2f-default-rtdb.firebaseio.com/")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase Connected");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // ✅ ADD TASK
    @PostMapping
    public Map<String, Object> addTask(@RequestBody Map<String, Object> task) throws Exception {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("tasks");

        task.put("status", "pending");

        ApiFuture<Void> future = ref.push().setValueAsync(task);
        future.get();

        return task;
    }

    // ✅ GET ALL TASKS (FIXED PROPERLY)
    @GetMapping
    public CompletableFuture<List<Object>> getTasks() {

        CompletableFuture<List<Object>> result = new CompletableFuture<>();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("tasks");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                List<Object> tasks = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    tasks.add(child.getValue());
                }

                result.complete(tasks);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                result.completeExceptionally(error.toException());
            }
        });

        return result;
    }

    // ✅ DELETE TASK
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable String id) throws Exception {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("tasks")
                .child(id);

        ApiFuture<Void> future = ref.removeValueAsync();
        future.get();

        return "Task Deleted Successfully";
    }
}