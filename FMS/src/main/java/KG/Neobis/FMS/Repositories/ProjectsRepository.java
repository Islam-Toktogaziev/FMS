package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectsRepository extends JpaRepository<Projects,Long> {

    Optional<Projects> findByNameAndDeletedFalse (String name);

    List<Projects> findAllByDeletedFalseAndArchivedFalse();

    List<Projects> findAllByDeletedFalse();

    boolean existsByNameAndDeletedFalse(String name);
}
