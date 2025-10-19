package com.aperegarc.gestor_tareas.Controller;

import com.aperegarc.gestor_tareas.entity.Task;
import com.aperegarc.gestor_tareas.entity.User;
import com.aperegarc.gestor_tareas.repository.TaskRepository;
import com.aperegarc.gestor_tareas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private Optional<User> getAuthenticatedUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername());
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllUserTask(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = getAuthenticatedUser(userDetails);

        if (userOptional.isPresent()) {
            List<Task> tasks = taskRepository.findByOwner(userOptional.get());
            return ResponseEntity.ok(tasks);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task newTask,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = getAuthenticatedUser(userDetails);

        if (userOptional.isPresent()) {
            User owner = userOptional.get();
            newTask.setOwner(owner);
            Task savedTask = taskRepository.save(newTask);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody Task taskDetails,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Task> taskOptional = taskRepository.findById(id);
        Optional<User> userOptional = getAuthenticatedUser(userDetails);

        if (taskOptional.isEmpty() || userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task existingTask = taskOptional.get();
        User owner = userOptional.get();

        if (!existingTask.getOwner().getId().equals(owner.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setCompleted(taskDetails.isCompleted());

        Task updatedTask = taskRepository.save(existingTask);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Task> taskOptional = taskRepository.findById(id);
        Optional<User> userOptional = getAuthenticatedUser(userDetails);

        if (taskOptional.isEmpty() || userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Task existingTask = taskOptional.get();
        User owner = userOptional.get();

        if (!existingTask.getOwner().getId().equals(owner.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        taskRepository.delete(existingTask);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
