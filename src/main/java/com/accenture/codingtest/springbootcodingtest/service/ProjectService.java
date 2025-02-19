package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.Project;
import com.accenture.codingtest.springbootcodingtest.model.ProjectDto;
import com.accenture.codingtest.springbootcodingtest.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class contains all the business logic for projects.
 * @author Parag Sansare
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<ProjectDto> findAll(String q, int pageIndex, int pageSize, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(direction, sortBy));
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(orders));
        Page<Project> pageProjects;
        if (q == null)
            pageProjects = projectRepository.findAll(pageable);
        else
            pageProjects = projectRepository.findAllByNameContaining(q, pageable);
        List<Project> projects = pageProjects.getContent();
        return projects.stream().map(projectEntity -> modelMapper.map(projectEntity, ProjectDto.class)).collect(Collectors.toList());
    }

    public ProjectDto findById(UUID id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty()) {
            throw new RuntimeException("Project not found");
        }
        return modelMapper.map(optionalProject.get(),ProjectDto.class);
    }

    public void create(ProjectDto projectDto) throws Exception {
        Project ProjectByProjectname = projectRepository.findByName(projectDto.getName());
        if (ProjectByProjectname != null)
            throw new Exception("Project Name is taken");
        projectRepository.save(modelMapper.map(projectDto, Project.class));
    }

    public void update(UUID id, ProjectDto projectDto) throws Exception {
        Project projectByProjectName = projectRepository.findByName(projectDto.getName());
        if (projectByProjectName != null)
            throw new Exception("Project Name is taken");
        Project project =  new Project();
        project.setId(id);
        project.setName(projectDto.getName());
        projectRepository.save(project);
    }

    public void patch(UUID id, ProjectDto projectDto) throws Exception {
        Project Project = projectRepository.findById(id).orElseThrow();
        boolean needUpdate  = false;
        if (StringUtils.hasLength(projectDto.getName())) {
            Project ProjectByProjectname = projectRepository.findByName(projectDto.getName());
            if (ProjectByProjectname != null)
                throw new Exception("Project Name is taken");
            Project.setName(projectDto.getName());
            needUpdate  = true;
        }
        if (needUpdate )
            projectRepository.save(Project);
    }

    public void deleteById(UUID id) {
        projectRepository.deleteById(id);
    }
}
