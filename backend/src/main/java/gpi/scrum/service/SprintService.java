package gpi.scrum.service;

import gpi.scrum.domain.Sprint;
import gpi.scrum.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SprintService {

    private final SprintRepository sprintRepository;
    

    @Autowired
    public SprintService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    public List<Sprint> getAllUserStories() {
        return sprintRepository.findAll();
    }

    public Optional<Sprint> getSprintById(Integer id) {
        return sprintRepository.findById(id);
    }

    public List<Sprint> getUserStoriesByProjectId(Integer projectId) {
        return sprintRepository.findByProjectId(projectId);
    }

    public Sprint createSprint(Sprint Sprint) {
        validateSprintDates(Sprint);
        return sprintRepository.save(Sprint);
    }

    public Sprint updateSprint(Integer id, Sprint sprintDetails) {
        Optional<Sprint> optionalSprint = sprintRepository.findById(id);
        if (optionalSprint.isPresent()) {
            Sprint sprint = optionalSprint.get();
            sprint.setName(sprintDetails.getName());
            sprint.setDescription(sprintDetails.getDescription());
            sprint.setStartDate(sprintDetails.getStartDate());
            sprint.setEndDate(sprintDetails.getEndDate());
            validateSprintDates(sprint);
            return sprintRepository.save(sprint);
        } else {
            throw new RuntimeException("sprint not found");
        }
    }

    public void deleteSprint(Integer id) {
        sprintRepository.deleteById(id);
    }

    public Sprint changeSprintState(Integer id, String newState) {
        Optional<Sprint> optionalSprint = sprintRepository.findById(id);
        if (optionalSprint.isPresent()) {
            Sprint sprint = optionalSprint.get();
            sprint.setState(Sprint.State.valueOf(newState));
            return sprintRepository.save(sprint);
        } else {
            throw new RuntimeException("Sprint not found");
        }
    }

    private void validateSprintDates(Sprint sprint) {
        if (sprint.getStartDate().after(sprint.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
}