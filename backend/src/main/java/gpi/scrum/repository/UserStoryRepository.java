package gpi.scrum.repository;

import gpi.scrum.domain.UserStory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Integer> {

    @Query("SELECT u FROM UserStory u WHERE u.project.id = ?1")
    List<UserStory> findByProjectId(Integer projectId);

    void deleteByProjectId(Integer id);
}