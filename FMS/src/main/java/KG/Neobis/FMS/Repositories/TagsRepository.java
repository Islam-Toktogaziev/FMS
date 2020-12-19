package KG.Neobis.FMS.Repositories;

import KG.Neobis.FMS.Entities.TransactionTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<TransactionTags, Long> {

    Optional<TransactionTags> findByName (String name);

    List<TransactionTags> findAllByDeletedFalseAndArchivedFalse();

    List<TransactionTags> findAllByDeletedFalse();
}
