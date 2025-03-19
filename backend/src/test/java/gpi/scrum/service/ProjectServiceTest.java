package gpi.scrum.service;

import gpi.scrum.domain.Project;
import gpi.scrum.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project("Test Project", "Description", new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() + 10000), Arrays.asList("Collaborator1"), Project.State.IN_PROGRESS);
    }

    @Test
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));
        List<Project> projects = projectService.getAllProjects();
        assertEquals(1, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void testGetProjectById() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        Optional<Project> foundProject = projectService.getProjectById(1);
        assertTrue(foundProject.isPresent());
        assertEquals(project.getName(), foundProject.get().getName());
        verify(projectRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCreateProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project createdProject = projectService.createProject(project);
        assertNotNull(createdProject);
        assertEquals(project.getName(), createdProject.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testCreateProjectWithInvalidDates() {
        project.setStartDate(new Date(System.currentTimeMillis() + 20000));
        project.setEndDate(new Date(System.currentTimeMillis() + 10000));
        assertThrows(IllegalArgumentException.class, () -> projectService.createProject(project));
    }

    @Test
    void testUpdateProject() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project updatedProject = projectService.updateProject(1, project);
        assertNotNull(updatedProject);
        assertEquals(project.getName(), updatedProject.getName());
        verify(projectRepository, times(1)).findById(anyInt());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testUpdateProjectWithInvalidDates() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        project.setStartDate(new Date(System.currentTimeMillis() + 20000));
        project.setEndDate(new Date(System.currentTimeMillis() + 10000));
        assertThrows(IllegalArgumentException.class, () -> projectService.updateProject(1, project));
    }


    @Test
    void testChangeProjectState() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        Project.State newState = Project.State.IN_PROGRESS;
        Project updatedProject = projectService.changeProjectState(1, newState.name());
        assertNotNull(updatedProject);
        assertEquals(newState, updatedProject.getState());
        verify(projectRepository, times(1)).findById(anyInt());
        verify(projectRepository, times(1)).save(any(Project.class));
    }
}

