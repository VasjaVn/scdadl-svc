package refinitiv.scdadlsvc.dao.repository;

import refinitiv.scdadlsvc.dao.entity.ComponentGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentGroupRepository extends JpaRepository<ComponentGroupEntity, Long> {
    ComponentGroupEntity findByName(String componentGroupName);
}
