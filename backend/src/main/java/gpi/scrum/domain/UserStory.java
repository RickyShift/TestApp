package gpi.scrum.domain;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class UserStory {

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

    @ManyToOne()
    @JsonBackReference
    private Project project;

    @ManyToOne()
    private Sprint sprint;

    public enum State {
        TO_DO,
        IN_PROGRESS,
        COMPLETED
    }

    @Enumerated(EnumType.STRING)
    private State state;

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    @Enumerated(EnumType.STRING)
    private Priority priority;


    public UserStory() {}

    public UserStory(String name, String description, Date startDate, Date endDate, 
                     List<String> collaborators, State state, Priority priority, Project project) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.collaborators = collaborators;
        this.state = state;
        this.priority = priority;
        this.project = project;
        if (this.project != null){
            this.project.addUserStory(this);
        } 
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}