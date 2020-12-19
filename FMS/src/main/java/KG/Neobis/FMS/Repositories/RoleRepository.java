package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Users.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Roles, Long> {

    boolean existsByName(String name);

    Roles findAllByNameAndDeletedFalse(String name);

    boolean existsByNameAndDeletedFalse (String name);

    List<Roles> findAllByDeletedFalseAndArchivedFalse();

    List<Roles> findAllByDeletedFalse();
}
