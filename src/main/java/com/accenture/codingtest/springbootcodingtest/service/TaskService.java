package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.Project;
import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.entity.User;
import com.accenture.codingtest.springbootcodingtest.model.TaskDto;
import com.accenture.codingtest.springbootcodingtest.repository.ProjectRepository;
import com.accenture.codingtest.springbootcodingtest.repository.TaskRepository;
import com.accenture.codingtest.springbootcodingtest.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class contains all the business logic for tasks.
 * @author Parag Sansare
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    private final ModelMapper modelMapper = new ModelMapper();
    
    public List<TaskDto> findAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public TaskDto findById(UUID id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found");
        }
        return entityToDto(taskOptional.get());
    }

    public void create(TaskDto taskDto) {
        projectRepository.findById(taskDto.getProjectId()).orElseThrow();
        userRepository.findById(taskDto.getUserId()).orElseThrow();
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus("NOT_STARTED");
        User user = new User();
        user.setId(taskDto.getUserId());
        task.setUser(user);
        Project project  = new Project();
        project.setId(taskDto.getProjectId());
        task.setProject(project);
        taskRepository.save(task);
    }

    public void update(UUID id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow();
        projectRepository.findById(taskDto.getProjectId()).orElseThrow();
        userRepository.findById(taskDto.getUserId()).orElseThrow();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus("NOT_STARTED");
        User user = new User();
        user.setId(taskDto.getUserId());
        task.setUser(user);
        Project project  = new Project();
        project.setId(taskDto.getProjectId());
        task.setProject(project);
        taskRepository.save(task);
    }

    public void patch(UUID id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow();
        projectRepository.findById(taskDto.getProjectId()).orElseThrow();
        userRepository.findById(taskDto.getUserId()).orElseThrow();
        boolean requiredUpdate = false;
        if (StringUtils.hasLength(taskDto.getTitle())) {
            task.setTitle(taskDto.getTitle());
            requiredUpdate = true;
        }
        if (StringUtils.hasLength(taskDto.getDescription())) {
            task.setDescription(taskDto.getDescription());
            requiredUpdate = true;
        }
        if (StringUtils.hasLength(taskDto.getStatus())) {
            task.setStatus(taskDto.getStatus());
            requiredUpdate = true;
        }
        if (requiredUpdate)
            taskRepository.save(task);
    }

    public void deleteById(UUID id) {
        taskRepository.deleteById(id);
    }

    private TaskDto entityToDto(Task task) {
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        taskDto.setUserId(task.getUser().getId());
        taskDto.setProjectId(task.getProject().getId());
        return taskDto;
    }
}
