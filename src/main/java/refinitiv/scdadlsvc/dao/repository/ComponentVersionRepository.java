package refinitiv.scdadlsvc.dao.repository;

import refinitiv.scdadlsvc.dao.entity.ComponentVersionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentVersionRepository extends JpaRepository<ComponentVersionEntity, Long> {

    @Query(value = "SELECT * FROM COMPONENT_VERSION WHERE component_fk IN (SELECT c.id FROM COMPONENT c WHERE c.name LIKE :componentName%)",
           nativeQuery = true)
    Page<ComponentVersionEntity> searchByComponentName(@Param("componentName") String search, Pageable pageable);
}
