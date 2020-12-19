package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogRepository extends JpaRepository<ChangeLog,Long> {
}
