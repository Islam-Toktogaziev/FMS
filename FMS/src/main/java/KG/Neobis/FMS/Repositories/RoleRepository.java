package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Users.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Roles, Long> {

    Roles findByName (String name);
    boolean existsByName (String name);
}
