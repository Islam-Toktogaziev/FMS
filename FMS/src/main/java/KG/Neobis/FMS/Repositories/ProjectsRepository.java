package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectsRepository extends JpaRepository<Projects,Long> {

    Optional<Projects> findByName (String name);
}
