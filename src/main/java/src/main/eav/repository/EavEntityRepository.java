package src.main.eav.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import src.main.eav.model.EavEntity;

public interface EavEntityRepository extends JpaSpecificationExecutor<EavEntity>, JpaRepository<EavEntity, Long> {
}
