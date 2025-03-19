package gpi.scrum.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    private Date startDate;

    private Date endDate;

    private List<String> collaborators;

    public enum State {
        IN_PROGRESS,
        COMPLETED
    }

    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference 
    private List<UserStory> productBacklog;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL) 
    private List<Sprint> sprints;

    public Project() {
    }

    public Project(String name, String description, Date startDate, Date endDate, List<String> collaborators,
            State state) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.collaborators = collaborators;
        this.state = state;
        this.productBacklog = new ArrayList<>();
        this.sprints = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<UserStory> getProductBacklog() {
        return productBacklog;
    }

    public void setProductBacklog(ArrayList<UserStory> productBacklog) {
        this.productBacklog = productBacklog;
    }

    public void addUserStory(UserStory userStory) {
        this.productBacklog.add(userStory);
    }

    public void removeUserStory(UserStory userStory) {
        this.productBacklog.remove(userStory);
    }

    public void addSprint(Sprint sprint) {
        this.sprints.add(sprint);
    }

    public void removeSprint(Sprint sprint) {
        this.sprints.remove(sprint);
    }
}
