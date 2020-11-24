package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Users.AppUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUsers, Long> {

    Optional<AppUsers> findByUsername (String username);
    Optional<AppUsers> findByEmail (String email);
    Optional<AppUsers> findByRecoveryCode (String code);
    boolean existsByEmail (String email);
    boolean existsByUsername (String username);
}
