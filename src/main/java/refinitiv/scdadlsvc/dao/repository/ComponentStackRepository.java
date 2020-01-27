package refinitiv.scdadlsvc.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import refinitiv.scdadlsvc.dao.entity.ComponentStackEntity;

@Repository
public interface ComponentStackRepository extends JpaRepository<ComponentStackEntity, Long> {
    @Query(value = "SELECT cse FROM ComponentStackEntity cse WHERE cse.name LIKE :search%")
    Page<ComponentStackEntity> searchByComponentStackName(@Param("search") String search, Pageable pageable);

    ComponentStackEntity findByName(String componentStackName);
}