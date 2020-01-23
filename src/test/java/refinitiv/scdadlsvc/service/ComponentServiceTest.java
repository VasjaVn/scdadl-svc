package refinitiv.scdadlsvc.service;

import org.mockito.Mock;
import org.springframework.data.domain.Page;
import refinitiv.scdadlsvc.dao.entity.ComponentEntity;
import refinitiv.scdadlsvc.dao.entity.ComponentGroupEntity;
import refinitiv.scdadlsvc.dao.entity.Metadata;
import refinitiv.scdadlsvc.dao.entity.PlatformEntity;
import refinitiv.scdadlsvc.rest.dto.ComponentDto;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ComponentAlreadyExistException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.component.CreateComponentWithWrongGroupNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.component.CreateComponentWithWrongPlatformNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ComponentNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.searchobject.SearchComponentsEmptyListException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongGroupNameException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongIdException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component.UpdateComponentWithWrongPlatformNameException;
import refinitiv.scdadlsvc.service.impl.ComponentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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
public class ComponentServiceTest extends AbstractServiceTest {
    private static final String COMPONENT_NAME_FOR_CREATE = "component_name_for_create";

    private ComponentServiceImpl componentService;
    private ComponentDto componentDto;

    @Mock
    private Page<ComponentEntity> pageMock;

    @Before
    public void setUp() {
        componentDto = ComponentDto.builder()
                .id(COMPONENT_ID)
                .componentName(COMPONENT_NAME_FOR_CREATE)
                .componentGroupName(COMPONENT_GROUP_NAME)
                .platformName(COMPONENT_PLATFORM_NAME)
                .assetInsightId(COMPONENT_ASSET_INSIGHT_ID)
                .build();

        Metadata metadata = Metadata.builder()
                .createdDate(CREATED_DATE)
                .createdBy(CREATED_BY)
                .updatedDate(UPDATED_DATE)
                .updatedBy(UPDATED_BY)
                .build();

        PlatformEntity platformEntity = PlatformEntity.builder()
                .id(COMPONENT_PLATFORM_ID)
                .name(COMPONENT_PLATFORM_NAME)
                .metadata(metadata)
                .build();

        ComponentGroupEntity componentGroupEntity = ComponentGroupEntity.builder()
                .id(COMPONENT_GROUP_ID)
                .name(COMPONENT_GROUP_NAME)
                .platform(platformEntity)
                .metadata(metadata)
                .build();

        ComponentEntity componentEntity = ComponentEntity.builder()
                .id(COMPONENT_ID)
                .name(COMPONENT_NAME)
                .componentGroup(componentGroupEntity)
                .assetInsightId(COMPONENT_ASSET_INSIGHT_ID)
                .metadata(metadata)
                .build();

        componentGroupEntity.setComponents(new ArrayList<>(List.of(componentEntity)));
        platformEntity.setComponentGroups(new ArrayList<>(List.of(componentGroupEntity)));

        when(platformRepositoryMock.findByName(anyString())).thenReturn(platformEntity);

        when(componentGroupRepositoryMock.findByName(anyString())).thenReturn(componentGroupEntity);

        when(pageMock.getContent()).thenReturn(List.of(componentEntity));
        when(componentRepositoryMock.searchByComponentName(anyString(), any())).thenReturn(pageMock);
        when(componentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(componentEntity));

        componentService = new ComponentServiceImpl();
        componentService.setPlatformRepository(platformRepositoryMock);
        componentService.setComponentRepository(componentRepositoryMock);
        componentService.setComponentGroupRepository(componentGroupRepositoryMock);
    }

    /*************************************************
     *  TESTS - CREATE_COMPONENT - createComponent()
     *************************************************/
    @Test
    public void testCreateComponentSuccess() {
        // given

        // when
        componentService.createComponent(componentDto);

        // then
        verify(componentRepositoryMock, times(1)).save(any());
    }

    @Test(expected = ComponentAlreadyExistException.class)
    public void testCreateComponentFailsWhenComponentAlreadyExist() {
        // given
        componentDto.setComponentName(COMPONENT_NAME);

        // when
        componentService.createComponent(componentDto);

        // then
    }

    @Test(expected = CreateComponentWithWrongPlatformNameException.class)
    public void testCreateComponentFailsWhenWrongPlatformName() {
        // given
        componentDto.setPlatformName("WRONG_PLATFORM_NAME");
        when(platformRepositoryMock.findByName(anyString())).thenReturn(null);

        // when
        componentService.createComponent(componentDto);

        // then
    }

    @Test(expected = CreateComponentWithWrongGroupNameException.class)
    public void testCreateComponentFailsWhenWrongComponentGroupName() {
        // given
        componentDto.setComponentGroupName("WRONG_COMPONENT_GROUP_NAME");

        // when
        componentService.createComponent(componentDto);

        // then
    }

    /****************************************************
     *  TESTS - GET_COMPONENT_BY_ID - createComponent()
     ****************************************************/
    @Test
    public void testGetComponentByIdSuccess() {
        // given

        // when
        ComponentEntity componentEntity = componentService.getComponentById(111L);

        // then
        assertEquals(COMPONENT_ID, componentEntity.getId());
        assertEquals(COMPONENT_NAME, componentEntity.getName());
        assertEquals(COMPONENT_ASSET_INSIGHT_ID, componentEntity.getAssetInsightId());
        assertEquals(CREATED_DATE, componentEntity.getMetadata().getCreatedDate());
        assertEquals(CREATED_BY, componentEntity.getMetadata().getCreatedBy());
        assertEquals(UPDATED_DATE, componentEntity.getMetadata().getUpdatedDate());
        assertEquals(UPDATED_BY, componentEntity.getMetadata().getUpdatedBy());
        assertEquals(COMPONENT_GROUP_ID, componentEntity.getComponentGroup().getId());
        assertEquals(COMPONENT_GROUP_NAME, componentEntity.getComponentGroup().getName());
        assertEquals(COMPONENT_PLATFORM_ID, componentEntity.getComponentGroup().getPlatform().getId());
        assertEquals(COMPONENT_PLATFORM_NAME, componentEntity.getComponentGroup().getPlatform().getName());
    }

    @Test(expected = ComponentNotFoundException.class)
    public void testGetComponentByIdFailsWhenComponentNotFound() {
        // given
        when(componentRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        // when
        componentService.getComponentById(COMPONENT_ID);

        // then
    }

    /***************************************************
     *  TESTS - SEARCH_COMPONENTS - searchComponents()
     ***************************************************/
    @Test
    public void testSearchComponentsSuccess() {
        // given

        // when
        List<ComponentEntity> componentEntities = componentService.searchComponents(PAGE, LIMIT, SEARCH_PATTERN_COMPONENT_NAME);

        // then
        assertFalse(componentEntities.isEmpty());
    }

    @Test(expected = SearchComponentsEmptyListException.class)
    public void testSearchComponentsFailsWhenNotFoundComponents() {
        // given
        when(pageMock.getContent()).thenReturn(new ArrayList<>());
        when(componentRepositoryMock.searchByComponentName(anyString(), any())).thenReturn(pageMock);

        // when
        componentService.searchComponents(PAGE, LIMIT, SEARCH_PATTERN_COMPONENT_NAME);

        // then
    }

    /*************************************************
     *  TESTS - UPDATE_COMPONENT - updateComponent()
     *************************************************/
    @Test
    public void testUpdateComponentSuccess() {
        // given
        componentDto.setComponentName("NEW_COMPONENT_NAME");

        // when
        componentService.updateComponent(COMPONENT_ID, componentDto);

        // then
    }

    @Test(expected = UpdateComponentWithWrongPlatformNameException.class)
    public void testUpdateComponentFailsWhenWrongPartnerName() {
        // given
        componentDto.setPlatformName("WRONG_PLATFORM_NAME");
        when(platformRepositoryMock.findByName("WRONG_PLATFORM_NAME")).thenReturn(null);

        // when
        componentService.updateComponent(COMPONENT_ID, componentDto);

        // then
    }

    @Test(expected = UpdateComponentWithWrongGroupNameException.class)
    public void testUpdateComponentFailsWhenWrongComponentGroupName() {
        // given
        componentDto.setComponentGroupName("WRONG_COMPONENT_GROUP_NAME");
        when(componentGroupRepositoryMock.findByName("WRONG_COMPONENT_GROUP_NAME")).thenReturn(null);

        // when
        componentService.updateComponent(COMPONENT_ID, componentDto);

        // then
    }

    @Test(expected = UpdateComponentWithWrongIdException.class)
    public void testUpdateComponentFailsWhenWrongId() {
        // given
        when(componentRepositoryMock.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        // when
        componentService.updateComponent(COMPONENT_ID, componentDto);

        // then
    }
}
