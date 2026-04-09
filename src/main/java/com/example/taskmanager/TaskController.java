package com.example.taskmanager;

import com.google.firebase.database.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tasks")
@CrossOrigin("*")
public class TaskController {

    @PostMapping
    public String addTask(@RequestBody Map<String, Object> task) {

        DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("tasks");

        ref.push().setValueAsync(task);

        return "Task Added Successfully";
    }
}