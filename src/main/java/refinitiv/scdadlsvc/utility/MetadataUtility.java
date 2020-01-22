package refinitiv.scdadlsvc.utility;

import refinitiv.scdadlsvc.dao.entity.Metadata;

import java.util.Date;

public abstract class MetadataUtility {
    public static Metadata withOnlyCreatedData(String createdBy) {
        return Metadata.builder()
                .createdDate(new Date())
                .createdBy(createdBy)
                .updatedDate(null)
                .updatedBy(null)
                .build();
    }
}
