package treat_app.web_service.service.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import treat_app.web_service.ObjectFactory;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.service.dto.TreatDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TreatMapperImpl.class})
public class TreatMapperTest {

    @Autowired
    TreatMapper treatMapper;

    @Test
    public void toDomain_correctDto_returnValidTreatEntity() {
        //given
        TreatDto dto = ObjectFactory.TreatDto_userId(1L);

        //when
        Treat result = treatMapper.toEntity(dto);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(dto);
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getAmount()).isEqualTo(dto.getAmount());
        assertThat(result.getIncreaseBy()).isEqualTo(dto.getIncreaseBy());
        assertThat(result.getPic()).isEqualTo(dto.getPic());
        assertThat(result.getUser().getId()).isEqualTo(dto.getUserId());
    }

    @Test
    public void toDomain_nullDto_returnsNull() {
        //when
        Treat result = treatMapper.toEntity(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void toDto_correctEntity_returnsValidTreatDto() {
        //given
        Treat treat = ObjectFactory.Treat_user(ObjectFactory.User());

        //when
        TreatDto result = treatMapper.toDto(treat);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(treat);
        assertThat(result.getId()).isEqualTo(treat.getId());
        assertThat(result.getName()).isEqualTo(treat.getName());
        assertThat(result.getAmount()).isEqualTo(treat.getAmount());
        assertThat(result.getIncreaseBy()).isEqualTo(treat.getIncreaseBy());
        assertThat(result.getPic()).isEqualTo(treat.getPic());
        assertThat(result.getUserId()).isEqualTo(treat.getUser().getId());
    }

    @Test
    public void toDto_nullEntity_returnsNull() {
        //when
        TreatDto result = treatMapper.toDto(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void toTreatDtos_treatEntieties_returnTreatDtos() {
        //given
        List<Treat> entieties = new ArrayList<>(Arrays.asList(ObjectFactory.Treat_user(ObjectFactory.User()), ObjectFactory.Treat_id_name_user(2L, "bob", ObjectFactory.User())));

        //when
        List<TreatDto> result = treatMapper.toTreatDtos(entieties);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSameSizeAs(entieties);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i)).isEqualToComparingOnlyGivenFields(entieties.get(i), "id", "name", "amount", "increaseBy", "pic");
            assertThat(result.get(i).getUserId()).isEqualTo(entieties.get(i).getUser().getId());
        }
    }

    @Test
    public void toTreatDtos_null_returnsNull() {
        //when
        List<TreatDto> result = treatMapper.toTreatDtos(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void toTreatDtos_emptyList_returnsEmptyList() {
        //when
        List<TreatDto> result = treatMapper.toTreatDtos(Collections.emptyList());

        //then
        assertThat(result).isEmpty();
    }

    @Test
    public void toTreatEntities_treatDtos_returnTreatEntities() {
        //given
        List<TreatDto> dtos = new ArrayList<>(Arrays.asList(ObjectFactory.TreatDto_userId(ObjectFactory.User().getId()), ObjectFactory.TreatDto_id_name_userId(2L, "bob", ObjectFactory.User().getId())));

        //when
        List<Treat> result = treatMapper.toTreatEntities(dtos);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSameSizeAs(dtos);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i)).isEqualToComparingOnlyGivenFields(dtos.get(i), "id", "name", "amount", "increaseBy", "pic");
            assertThat(result.get(i).getUser().getId()).isEqualTo(dtos.get(i).getUserId());
        }
    }

    @Test
    public void toTreatEntities_null_returnsNull() {
        //when
        List<Treat> result = treatMapper.toTreatEntities(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void toTreat_emptyList_returnsEmptyList() {
        //when
        List<Treat> result = treatMapper.toTreatEntities(Collections.emptyList());

        //then
        assertThat(result).isEmpty();
    }
}