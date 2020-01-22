package refinitiv.scdadlsvc.dao.repository;

import refinitiv.scdadlsvc.dao.entity.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<PlatformEntity, Long> {
    PlatformEntity findByName(String platformName);
}
