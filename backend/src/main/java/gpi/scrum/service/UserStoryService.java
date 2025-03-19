package gpi.scrum.service;

import gpi.scrum.domain.Project;
import gpi.scrum.domain.UserStory;
import gpi.scrum.dto.UserStoryDto;
import gpi.scrum.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final ProjectService projectService;
    

    @Autowired
    public UserStoryService(UserStoryRepository userStoryRepository, ProjectService projectService) {
        this.userStoryRepository = userStoryRepository;
        this.projectService = projectService;
    }

    public List<UserStory> getAllUserStories() {
        return userStoryRepository.findAll();
    }

    public Optional<UserStory> getUserStoryById(Integer id) {
        return userStoryRepository.findById(id);
    }

    public List<UserStory> getUserStoriesByProjectId(Integer projectId) {
        return userStoryRepository.findByProjectId(projectId);
    }

    public UserStory createUserStory(UserStoryDto userStoryDto) {
        Project project = projectService.getProjectById(userStoryDto.getProjectId()).orElseThrow(() -> new RuntimeException("Project not found"));
        UserStory userStory = new UserStory(userStoryDto.getName(), userStoryDto.getDescription(), userStoryDto.getStartDate(), userStoryDto.getEndDate(), userStoryDto.getCollaborators(), UserStory.State.valueOf(userStoryDto.getState()), UserStory.Priority.valueOf(userStoryDto.getPriority()), project);
        validateUserStoryDates(userStory);
        return userStoryRepository.save(userStory);
    }

    public UserStory updateUserStory(Integer id, UserStory userStoryDetails) {
        Optional<UserStory> optionalUserStory = userStoryRepository.findById(id);
        if (optionalUserStory.isPresent()) {
            UserStory userStory = optionalUserStory.get();
            userStory.setName(userStoryDetails.getName());
            userStory.setDescription(userStoryDetails.getDescription());
            userStory.setStartDate(userStoryDetails.getStartDate());
            userStory.setEndDate(userStoryDetails.getEndDate());
            validateUserStoryDates(userStory);
            return userStoryRepository.save(userStory);
        } else {
            throw new RuntimeException("user story not found");
        }
    }

    public void deleteUserStory(Integer id) {
        userStoryRepository.deleteById(id);
    }


    private void validateUserStoryDates(UserStory userStory) {
        if (userStory.getStartDate().after(userStory.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }


    public Project getProject(Integer id) {
        return getUserStoryById(id).get().getProject();
    }
}