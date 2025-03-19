package gpi.scrum.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class ProjectTest {

    @Test
    public void testProjectConstructorAndGetters() {
        String name = "Project A";
        String description = "Description of Project A";
        Date startDate = new Date();
        Date endDate = new Date();
        List<String> collaborators = Arrays.asList("Alice", "Bob");
        Project.State state = Project.State.IN_PROGRESS;

        Project project = new Project(name, description, startDate, endDate, collaborators, state);

        assertEquals(name, project.getName());
        assertEquals(description, project.getDescription());
        assertEquals(startDate, project.getStartDate());
        assertEquals(endDate, project.getEndDate());
        assertEquals(collaborators, project.getCollaborators());
        assertEquals(state, project.getState());
    }

    @Test
    public void testSetters() {
        Project project = new Project();

        String name = "Project B";
        String description = "Description of Project B";
        Date startDate = new Date();
        Date endDate = new Date();
        List<String> collaborators = Arrays.asList("Charlie", "Dave");
        Project.State state = Project.State.IN_PROGRESS;

        project.setName(name);
        project.setDescription(description);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setCollaborators(collaborators);
        project.setState(state);

        assertEquals(name, project.getName());
        assertEquals(description, project.getDescription());
        assertEquals(startDate, project.getStartDate());
        assertEquals(endDate, project.getEndDate());
        assertEquals(collaborators, project.getCollaborators());
        assertEquals(state, project.getState());
    }

    @Test
    public void testIdSetterAndGetter() {
        Project project = new Project();
        Integer id = 1;
        project.setId(id);
        assertEquals(id, project.getId());
    }
}