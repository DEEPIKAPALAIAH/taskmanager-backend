package com.example.taskmanager;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

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

    // ✅ GET TASKS
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