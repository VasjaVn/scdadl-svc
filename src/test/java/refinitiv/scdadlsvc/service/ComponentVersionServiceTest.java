package refinitiv.scdadlsvc.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import refinitiv.scdadlsvc.dao.entity.ComponentEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentVersionEntity;
import refinitiv.scdadlsvc.rest.dto.ComponentVersionDto;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ReqParamIdAndDtoIdNotEqualsException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentVersionsNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ScdadlObjectNotFoundException;
import refinitiv.scdadlsvc.service.impl.ComponentVersionServiceImpl;
import refinitiv.scdadlsvc.utility.MetadataUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComponentVersionServiceTest extends AbstractServiceTest {
    @Mock
    private Page<ComponentVersionEntity> pageMock;

    private ComponentVersionDto componentVersionDto;
    private ComponentVersionServiceImpl componentVersionService;

    @Before
    public void setUp() {
        componentVersionDto = ComponentVersionDto.builder()
                .id(COMPONENT_VERSION_ID)
                .version(COMPONENT_VERSION)
                .packageUrl(COMPONENT_VERSION_PACKAGE_URL)
                .format(COMPONENT_VERSION_FORMAT)
                .qualityGrade(COMPONENT_VERSION_QUALITY_GRADE)
                .versionValidated(COMPONENT_VERSION_VALIDATED)
                .versionAvoid(COMPONENT_VERSION_AVOID)
                .versionValidationError(COMPONENT_VERSION_VALIDATION_ERRORS)
                .build();

        ComponentVersionEntity componentVersionEntity = ComponentVersionEntity.builder()
                .id(COMPONENT_VERSION_ID)
                .version(COMPONENT_VERSION)
                .packageUrl(COMPONENT_VERSION_PACKAGE_URL)
                .format(COMPONENT_VERSION_FORMAT)
                .qualityGrade(COMPONENT_VERSION_QUALITY_GRADE)
                .versionValidated(COMPONENT_VERSION_VALIDATED)
                .versionAvoid(COMPONENT_VERSION_AVOID)
                .versionValidationError(COMPONENT_VERSION_VALIDATION_ERRORS)
                .metadata(MetadataUtility.withOnlyCreatedData("test_created_by"))
                .build();

        List<ComponentVersionEntity> componentVersionEntities = new ArrayList<>();
        componentVersionEntities.add(componentVersionEntity);

        ComponentEntity componentEntity = ComponentEntity.builder()
                .id(COMPONENT_ID)
                .name(COMPONENT_NAME)
                .metadata(MetadataUtility.withOnlyCreatedData("test_created_by"))
                .assetInsightId(COMPONENT_ASSET_INSIGHT_ID)
                .componentVersions(componentVersionEntities)
                .build();

        componentVersionEntity.setComponent(componentEntity);

        when(pageMock.getContent()).thenReturn(componentVersionEntities);
        when(componentVersionRepositoryMock.searchByComponentName(anyString(), any())).thenReturn(pageMock);
        when(componentVersionRepositoryMock.findById(anyLong())).thenReturn(Optional.of(componentVersionEntity));

        when(componentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(componentEntity));
        when(componentVersionRepositoryMock.save(any())).thenReturn(toComponentVersionEntityFromDto(componentVersionDto));

        componentVersionService = new ComponentVersionServiceImpl();
        componentVersionService.setComponentRepository(componentRepositoryMock);
        componentVersionService.setComponentVersionRepository(componentVersionRepositoryMock);
    }

    /***************************************************************
     *  TESTS - CREATE_COMPONENT_VERSION - createComponentVersion()
     ***************************************************************/
    @Test
    public void testCreateComponentVersionSuccess() {
        // given

        // when
        componentVersionService.createComponentVersion(COMPONENT_ID, componentVersionDto);

        // then
        verify(componentVersionRepositoryMock, times(1)).save(any());
    }

    @Test(expected = ScdadlObjectNotFoundException.class)
    public void testCreateComponentVersionFailsWhenComponentIdIsNotFound() {
        // given
        when(componentRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        // when
        componentVersionService.createComponentVersion(COMPONENT_ID, componentVersionDto);

        // then
    }

    /********************************************************************
     *  TESTS - GET_COMPONENT_VERSION_BY_ID - getComponentVersionById()
     ********************************************************************/
    @Test
    public void testGetComponentVersionByIdSuccess() {
        // given

        // when
        ComponentVersionEntity componentVersionEntity =
                componentVersionService.getComponentVersionById(COMPONENT_VERSION_ID);

        // then
        assertEquals(COMPONENT_VERSION, componentVersionEntity.getVersion());
        assertEquals(COMPONENT_VERSION_PACKAGE_URL, componentVersionEntity.getPackageUrl());
        assertEquals(COMPONENT_VERSION_FORMAT, componentVersionEntity.getFormat());
        assertEquals(COMPONENT_VERSION_QUALITY_GRADE, componentVersionEntity.getQualityGrade());
        assertEquals(COMPONENT_VERSION_VALIDATED, componentVersionEntity.getVersionValidated());
        assertEquals(COMPONENT_VERSION_AVOID, componentVersionEntity.getVersionAvoid());
        assertEquals(COMPONENT_VERSION_VALIDATION_ERRORS, componentVersionEntity.getVersionValidationError());
    }

    @Test(expected = ScdadlObjectNotFoundException.class)
    public void testGetComponentVersionByIdFailsWhenComponentVersionIdIsNotFound() {
        // given
        when(componentVersionRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        // when
        componentVersionService.getComponentVersionById(COMPONENT_VERSION_ID);

        // then
    }

    /*****************************************************************************************
     *  TESTS - GET_COMPONENT_VERSIONS_BY_COMPONENT_ID - getComponentVersionsByComponentId()
     *****************************************************************************************/
    @Test
    public void testGetComponentVersionsByComponentIdSuccess() {
        // given

        // when
        List<ComponentVersionEntity> componentVersionEntities =
                componentVersionService.getComponentVersionsByComponentId(COMPONENT_ID);

        // then
        assertFalse(componentVersionEntities.isEmpty());
    }

    @Test(expected = ScdadlObjectNotFoundException.class)
    public void testGetComponentVersionsByComponentIdFailsWhenComponentNotFound() {
        // given
        when(componentRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        // when
        componentVersionService.getComponentVersionsByComponentId(COMPONENT_ID);

        // then
    }

    /******************************************************************
     *  TESTS - SEARCH_COMPONENT_VERSIONS - searchComponentVersions()
     ******************************************************************/
    @Test
    public void testSearchComponentVersionsSuccess() {
        // given

        // when
        List<ComponentVersionEntity> componentVersionEntities =
                componentVersionService.searchComponentVersions(PAGE, LIMIT, SEARCH_PATTERN_COMPONENT_NAME);

        // then
        assertFalse(componentVersionEntities.isEmpty());
    }

    @Test(expected = ComponentVersionsNotFoundException.class)
    public void testSearchComponentVersionsFailsWhenResultIsEmptyListOfComponentVersions() {
        // given
        when(pageMock.getContent()).thenReturn(List.of());
        when(componentVersionRepositoryMock.searchByComponentName(anyString(), any())).thenReturn(pageMock);

        // when
        componentVersionService.searchComponentVersions(PAGE, LIMIT, SEARCH_PATTERN_COMPONENT_NAME);

        // then
    }

    /*****************************************************************
     *  TESTS - UPDATE_COMPONENT_VERSIONS - updateComponentVersion()
     *****************************************************************/
    @Test
    public void testUpdateComponentVersionSuccess() {
        // given
        componentVersionDto.setVersion("UPDATE_VERSION");
        when(componentVersionRepositoryMock.save(any())).thenReturn(toComponentVersionEntityFromDto(componentVersionDto));

        // when
        componentVersionService.updateComponentVersion(COMPONENT_VERSION_ID, componentVersionDto);

        // then
        verify(componentVersionRepositoryMock, times(1)).save(any());
    }

    @Test(expected = ReqParamIdAndDtoIdNotEqualsException.class)
    public void testUpdateComponentVersionFailsWhenReqParIdAndDtoIdNotEqual() {
        // given
        final Long reqParId = 5L;
        when(componentVersionRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        // when
        componentVersionService.updateComponentVersion(reqParId, componentVersionDto);

        // then
    }

    @Test(expected = ScdadlObjectNotFoundException.class)
    public void testUpdateComponentVersionFailsWhenComponentVersionNotFound() {
        // given
        when(componentVersionRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        // when
        componentVersionService.updateComponentVersion(COMPONENT_VERSION_ID, componentVersionDto);

        // then
    }
}
