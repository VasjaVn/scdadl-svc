package refinitiv.scdadlsvc.utility;

import refinitiv.scdadlsvc.dao.entity.Metadata;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MetadataUtilityTest {
    private static final String CREATED_BY = "created_by";

    @Test
    public void testWithOnlyCreatedDataSuccess() {
        // given

        // when
        Metadata metadata = MetadataUtility.withOnlyCreatedData(CREATED_BY);

        // then
        assertNotNull(metadata.getCreatedDate());
        assertNotNull(metadata.getCreatedDate());
        assertEquals(CREATED_BY, metadata.getCreatedBy());
        assertNull(metadata.getUpdatedDate());
        assertNull(metadata.getUpdatedBy());
    }
}
