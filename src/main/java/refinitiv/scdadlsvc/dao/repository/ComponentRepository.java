package refinitiv.scdadlsvc.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import refinitiv.scdadlsvc.dao.entity.ComponentEntity;

@Repository
public interface ComponentRepository extends JpaRepository<ComponentEntity, Long> {
    @Query(value = "SELECT ce FROM ComponentEntity ce WHERE ce.name LIKE :componentName%")
    Page<ComponentEntity> searchByComponentName(@Param("componentName") String search, Pageable pageable);
}
