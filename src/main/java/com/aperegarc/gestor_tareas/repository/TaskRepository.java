package com.aperegarc.gestor_tareas.repository;

import java.util.List;

import com.aperegarc.gestor_tareas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aperegarc.gestor_tareas.entity.Task;

@Repository
public interface  TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByOwner(User owner);
}
