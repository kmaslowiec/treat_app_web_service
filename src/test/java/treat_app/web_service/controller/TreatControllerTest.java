package treat_app.web_service.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import treat_app.web_service.ObjectFactory;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.util.MyStrings;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TreatController.class)
public class TreatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TreatService treatService;

    @Test
    public void addTreat_validTreatDto_201httpResponse() throws Exception {
        //given
        TreatDto insertDto = ObjectFactory.TreatDto_userId(1L);
        insertDto.setId(null);
        TreatDto returnedDto = ObjectFactory.TreatDto_userId(1L);
        //when
        when(treatService.create(insertDto)).thenReturn(returnedDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/treat")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(returnedDto.getId()))
                .andExpect(jsonPath("$.name").value(returnedDto.getName()))
                .andExpect(jsonPath("$.amount").value(returnedDto.getAmount()))
                .andExpect(jsonPath("$.increaseBy").value(returnedDto.getIncreaseBy()))
                .andExpect(jsonPath("$.pic").value(returnedDto.getPic()))
                .andExpect(jsonPath("$.userId").value(returnedDto.getUserId()));
    }

    @Test
    public void addTreat_treatDtoIdIsNotNUll_400httpResponse() throws Exception {
        //given
        TreatDto insertDto = ObjectFactory.TreatDto_userId(1L);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/treat")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string(MyStrings.ID_ERROR, MyStrings.ID_ERROR_HAS_TO_BE_NULL));
    }

    @Test
    public void addTreat_treatDtoUserIdIsNull_400httpResponse() throws Exception {
        //given
        TreatDto insertDto = ObjectFactory.TreatDto_userId(null);
        insertDto.setId(null);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/treat")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string(MyStrings.TREAT_ID_ERROR, MyStrings.TREAT_USER_ID_CANNOT_BE_NULL));
    }
}