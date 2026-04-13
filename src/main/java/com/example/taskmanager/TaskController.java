package com.example.taskmanager;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("tasks");

    // ✅ ADD TASK
    @PostMapping
    public Map<String, Object> addTask(@RequestBody Map<String, Object> task) throws Exception {

        task.put("status", "pending");

        ApiFuture<Void> future = ref.push().setValueAsync(task);

        // safer for Render (can still throw if Firebase fails)
        future.get();

        return task;
    }

    // ✅ GET TASKS (FIXED: includes Firebase ID)
    @GetMapping
    public List<Map<String, Object>> getTasks() throws Exception {

        List<Map<String, Object>> tasks = new ArrayList<>();

        DataSnapshot snapshot = ref.get().get(); // synchronous safe fetch

        for (DataSnapshot child : snapshot.getChildren()) {

            Map<String, Object> task = (Map<String, Object>) child.getValue();

            if (task != null) {
                task.put("id", child.getKey()); // ⭐ IMPORTANT FIX
                tasks.add(task);
            }
        }

        return tasks;
    }

    // ✅ DELETE TASK (SAFE VERSION)
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable String id) throws Exception {

        if (id == null || id.isEmpty()) {
            return "Invalid ID";
        }

        ref.child(id).removeValueAsync();

        return "Task Deleted Successfully";
    }
}