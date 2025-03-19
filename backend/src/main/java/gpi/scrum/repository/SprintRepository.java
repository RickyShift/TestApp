package gpi.scrum.repository;

import gpi.scrum.domain.Sprint;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Integer> {

    @Query("SELECT u FROM Sprint u WHERE u.project.id = ?1")
    List<Sprint> findByProjectId(Integer projectId);

    void deleteByProjectId(Integer id);
}