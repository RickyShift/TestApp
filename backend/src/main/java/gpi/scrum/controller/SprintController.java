package gpi.scrum.controller;

import gpi.scrum.service.SprintService;
import gpi.scrum.domain.Sprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sprint")
@CrossOrigin
public class SprintController {

    private final SprintService sprintService;

    @Autowired
    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping
    public List<Sprint> getAllUserStories() {
        return sprintService.getAllUserStories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sprint> getSprintById(@PathVariable Integer id) {
        Optional<Sprint> sprint = sprintService.getSprintById(id);
        return sprint.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Sprint> createSprint(@RequestBody Sprint sprint) {
        Sprint createdSprint = sprintService.createSprint(sprint);
        return ResponseEntity.ok(createdSprint);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Sprint> updateSprint(@PathVariable Integer id, @RequestBody Sprint sprintDetails) {
        try {
            Sprint updatedSprint = sprintService.updateSprint(id, sprintDetails);
            return ResponseEntity.ok(updatedSprint);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Integer id) {
        sprintService.deleteSprint(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/changeState/{id}")
    public ResponseEntity<Sprint> changeSprintState(@PathVariable Integer id, @RequestParam String newState) {
        try {
            Sprint updatedSprint = sprintService.changeSprintState(id, newState);
            return ResponseEntity.ok(updatedSprint);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}