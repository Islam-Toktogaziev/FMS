package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.Users.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByAuthority (String authority);
    boolean existsByAuthority (String name);
}
