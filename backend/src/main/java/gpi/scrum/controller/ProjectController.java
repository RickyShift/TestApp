package gpi.scrum.controller;

import gpi.scrum.service.ProjectService;
import gpi.scrum.domain.Project;
import gpi.scrum.domain.UserStory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/userStories")
    public ResponseEntity<List<UserStory>> getUserStoriesOfProject(@PathVariable Integer id) {
        List<UserStory> userStories = projectService.getUserStoriesOfProject(id);
        return ResponseEntity.ok(userStories);
    }

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.ok(createdProject);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Integer id, @RequestBody Project projectDetails) {
        try {
            Project updatedProject = projectService.updateProject(id, projectDetails);
            return ResponseEntity.ok(updatedProject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/changeState/{id}")
    public ResponseEntity<Project> changeProjectState(@PathVariable Integer id, @RequestParam String newState) {
        try {
            Project updatedProject = projectService.changeProjectState(id, newState);
            return ResponseEntity.ok(updatedProject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
