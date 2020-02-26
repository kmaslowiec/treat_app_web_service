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

import java.util.ArrayList;
import java.util.List;

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
                .andExpect(header().string(MyStrings.TREAT_ID_ERROR, MyStrings.TREAT_USER_ID_ERROR_CANNOT_BE_NULL));
    }

    @Test
    public void addTreats_validListWithANUllIdAndUserId_201httpsResponse() throws Exception {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (String treatName : treatNames) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatName);
            insertedList.add(treatDto);
        }
        List<TreatDto> returnedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i + 1);
            treatDto.setName(treatNames[i]);
            returnedList.add(treatDto);
        }
        when(treatService.createMany(insertedList)).thenReturn(returnedList);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertedList)))
                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(returnedList.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(returnedList.get(0).getName()))
                .andExpect(jsonPath("$[0].amount").value(returnedList.get(0).getAmount()))
                .andExpect(jsonPath("$[0].increaseBy").value(returnedList.get(0).getIncreaseBy()))
                .andExpect(jsonPath("$[0].pic").value(returnedList.get(0).getPic()))
                .andExpect(jsonPath("$[0].userId").value(returnedList.get(0).getUserId()))
                .andExpect(jsonPath("$[1].id").value(returnedList.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(returnedList.get(1).getName()))
                .andExpect(jsonPath("$[1].amount").value(returnedList.get(1).getAmount()))
                .andExpect(jsonPath("$[1].increaseBy").value(returnedList.get(1).getIncreaseBy()))
                .andExpect(jsonPath("$[1].pic").value(returnedList.get(1).getPic()))
                .andExpect(jsonPath("$[1].userId").value(returnedList.get(1).getUserId()))
                .andExpect(jsonPath("$[2].id").value(returnedList.get(2).getId()))
                .andExpect(jsonPath("$[2].name").value(returnedList.get(2).getName()))
                .andExpect(jsonPath("$[2].amount").value(returnedList.get(2).getAmount()))
                .andExpect(jsonPath("$[2].increaseBy").value(returnedList.get(2).getIncreaseBy()))
                .andExpect(jsonPath("$[2].pic").value(returnedList.get(2).getPic()))
                .andExpect(jsonPath("$[2].userId").value(returnedList.get(2).getUserId()));
    }

    @Test
    public void addTreats_treatsAreNull_400httpsResponse() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(null)))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTreats_treatHaveNotNullID_400httpsResponse() throws Exception {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i + 1L);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertedList)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string(MyStrings.ID_ERROR, MyStrings.ID_ERROR_HAS_TO_BE_NULL));
    }

    @Test
    public void addTreats_TreatHasNullUserId_400httpsResponse() throws Exception {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (String treatName : treatNames) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(null);
            treatDto.setId(null);
            treatDto.setName(treatName);
            insertedList.add(treatDto);
        }
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertedList)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string(MyStrings.TREAT_ID_ERROR, MyStrings.TREAT_USER_ID_ERROR_CANNOT_BE_NULL));
    }

    @Test
    public void updateTreatsValidListWithNotNullIdAndUserId_201httpsResponse() throws Exception {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        List<TreatDto> returnedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i + 1);
            treatDto.setName(treatNames[i]);
            returnedList.add(treatDto);
        }
        when(treatService.updateMany(insertedList)).thenReturn(returnedList);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertedList)))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(returnedList.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(returnedList.get(0).getName()))
                .andExpect(jsonPath("$[0].amount").value(returnedList.get(0).getAmount()))
                .andExpect(jsonPath("$[0].increaseBy").value(returnedList.get(0).getIncreaseBy()))
                .andExpect(jsonPath("$[0].pic").value(returnedList.get(0).getPic()))
                .andExpect(jsonPath("$[0].userId").value(returnedList.get(0).getUserId()))
                .andExpect(jsonPath("$[1].id").value(returnedList.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(returnedList.get(1).getName()))
                .andExpect(jsonPath("$[1].amount").value(returnedList.get(1).getAmount()))
                .andExpect(jsonPath("$[1].increaseBy").value(returnedList.get(1).getIncreaseBy()))
                .andExpect(jsonPath("$[1].pic").value(returnedList.get(1).getPic()))
                .andExpect(jsonPath("$[1].userId").value(returnedList.get(1).getUserId()))
                .andExpect(jsonPath("$[2].id").value(returnedList.get(2).getId()))
                .andExpect(jsonPath("$[2].name").value(returnedList.get(2).getName()))
                .andExpect(jsonPath("$[2].amount").value(returnedList.get(2).getAmount()))
                .andExpect(jsonPath("$[2].increaseBy").value(returnedList.get(2).getIncreaseBy()))
                .andExpect(jsonPath("$[2].pic").value(returnedList.get(2).getPic()))
                .andExpect(jsonPath("$[2].userId").value(returnedList.get(2).getUserId()));
    }

    @Test
    public void updateTreats_treatsAreNull_400httpsResponse() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(null)))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTreats_treatHaveNullID_400httpsResponse() throws Exception {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (String treatName : treatNames) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatName);
            insertedList.add(treatDto);
        }
        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertedList)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string(MyStrings.ID_ERROR, MyStrings.ID_ERROR_CANNOT_BE_NULL));
    }

    @Test
    public void updateTreats_TreatHasNullUserId_400httpsResponse() throws Exception {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(null);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertedList)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string(MyStrings.TREAT_ID_ERROR, MyStrings.TREAT_USER_ID_ERROR_CANNOT_BE_NULL));
    }
}