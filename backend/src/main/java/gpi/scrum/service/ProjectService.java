package gpi.scrum.service;

import gpi.scrum.domain.Project;
import gpi.scrum.domain.UserStory;
import gpi.scrum.repository.ProjectRepository;
import gpi.scrum.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserStoryRepository userStoryRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserStoryRepository userStoryRepository) {
        this.projectRepository = projectRepository;
        this.userStoryRepository = userStoryRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Integer id) {
        return projectRepository.findById(id);
    }

    public List<UserStory> getUserStoriesOfProject(Integer id) {
        return getProjectById(id).map(Project::getProductBacklog).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project createProject(Project project) {
        validateProjectDates(project);
        if (projectRepository.findByName(project.getName()).isPresent()) {
            throw new IllegalArgumentException("Project with the same name already exists");
        }
        if (project.getState() == null) {
            project.setState(Project.State.IN_PROGRESS);
        }
        Project newProject = new Project(project.getName(), project.getDescription(), project.getStartDate(),
                project.getEndDate(), project.getCollaborators(), project.getState());
        return projectRepository.save(newProject);
    }

    public Project updateProject(Integer id, Project projectDetails) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setName(projectDetails.getName());
            project.setDescription(projectDetails.getDescription());
            project.setStartDate(projectDetails.getStartDate());
            project.setEndDate(projectDetails.getEndDate());
            validateProjectDates(project);
            return projectRepository.save(project);
        } else {
            throw new RuntimeException("Project not found");
        }
    }

    public void deleteProject(Integer id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
        Project project = optionalProject.get();
        // Assuming you have a UserStoryRepository to handle user stories
        userStoryRepository.deleteByProjectId(project.getId());
        projectRepository.deleteById(id);
        } else {
        throw new RuntimeException("Project not found");
        }
    }

    public Project changeProjectState(Integer id, String newState) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setState(Project.State.valueOf(newState));
            return projectRepository.save(project);
        } else {
            throw new RuntimeException("Project not found");
        }
    }

    private void validateProjectDates(Project project) {
        if (project.getStartDate().after(project.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
}
