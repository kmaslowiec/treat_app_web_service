package treat_app.web_service.service.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import treat_app.web_service.ObjectFactory;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.service.dto.TreatDto;

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
}