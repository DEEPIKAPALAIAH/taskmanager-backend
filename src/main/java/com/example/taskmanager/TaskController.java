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
        future.get(); // wait for Firebase write

        return task;
    }

    // ✅ GET TASKS
   @GetMapping
public List<Map<String, Object>> getTasks() throws Exception {

    List<Map<String, Object>> tasks = new ArrayList<>();

    final Object lock = new Object();

    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {

            for (DataSnapshot child : snapshot.getChildren()) {
                Map<String, Object> task = (Map<String, Object>) child.getValue();

                if (task != null) {
                    task.put("id", child.getKey());
                    tasks.add(task);
                }
            }

            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            synchronized (lock) {
                lock.notify();
            }
        }
    });

    synchronized (lock) {
        lock.wait(); // wait until Firebase responds
    }

    return tasks;
}
    // ✅ DELETE TASK
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable String id) {

        if (id == null || id.isEmpty()) {
            return "Invalid ID";
        }

        ref.child(id).removeValueAsync();

        return "Task Deleted Successfully";
    }
}