package refinitiv.scdadlsvc.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import refinitiv.scdadlsvc.dao.entity.ComponentStackVersionEntity;

@Repository
public interface ComponentStackVersionRepository extends JpaRepository<ComponentStackVersionEntity, Long> {

    @Query(value = "SELECT * FROM COMPONENT_STACK_VERSION WHERE component_stack_id IN (SELECT cs.id FROM COMPONENT_STACK cs WHERE cs.name LIKE :componentStackName%)",
           nativeQuery = true)
    Page<ComponentStackVersionEntity> searchByComponentStackName(@Param("componentStackName") String search, Pageable pageable);
}
