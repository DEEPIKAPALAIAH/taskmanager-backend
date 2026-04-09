package com.example.taskmanager;

import com.google.firebase.database.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tasks")
@CrossOrigin("*")
public class TaskController {

    // ADD TASK
    @PostMapping
    public String addTask(@RequestBody Map<String, Object> task) {

        DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("tasks");

        ref.push().setValueAsync(task);

        return "Task Added Successfully";
    }

    // GET TASKS
    @GetMapping
    public List<Object> getTasks() throws Exception {

        DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("tasks");

        final List<Object> tasks = new ArrayList<>();

        ref.get().addOnSuccessListener(snapshot -> {
            for (DataSnapshot data : snapshot.getChildren()) {
                tasks.add(data.getValue());
            }
        }).get(); // wait for result

        return tasks;
    }
}