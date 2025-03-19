package gpi.scrum.domain;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
public class Sprint {

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
    private Project project;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<UserStory> userStories;

    public enum State {
        TO_DO,
        IN_PROGRESS,
        COMPLETED
    }

    @Enumerated(EnumType.STRING)
    private State state;

    public Sprint() {}

    public Sprint(String name, String description, Date startDate, Date endDate, List<String> collaborators, State state, Project project) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.collaborators = collaborators;
        this.state = state;
        this.project = project;
        if (this.project != null){
            this.project.addSprint(this);
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}