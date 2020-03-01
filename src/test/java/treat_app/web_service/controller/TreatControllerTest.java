package treat_app.web_service.controller;

import org.junit.Ignore;
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
import treat_app.web_service.entity.User;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.util.MyStrings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
        when(treatService.createTreat(insertDto)).thenReturn(returnedDto);
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
    public void addTreats_validListWithANUllIdAndUserId_201httpResponse() throws Exception {
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
        when(treatService.createTreats(insertedList)).thenReturn(returnedList);

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
    public void addTreats_treatsAreNull_400httpResponse() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(null)))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTreats_treatHaveNotNullID_400httpResponse() throws Exception {
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
    public void addTreats_TreatHasNullUserId_400httpResponse() throws Exception {
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
    public void updateTreatsValidListWithNotNullIdAndUserId_201httpResponse() throws Exception {
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
        when(treatService.updateTreats(insertedList)).thenReturn(returnedList);

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
    public void updateTreats_treatsAreNull_400httpResponse() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/treat/many")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(null)))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTreats_treatHaveNullID_400httpResponse() throws Exception {
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
    public void updateTreats_TreatHasNullUserId_400httpResponse() throws Exception {
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

    @Test
    public void read_theIdIsInDb_200HttpResponse() throws Exception {
        //given
        TreatDto dto = ObjectFactory.TreatDto_userId(1L);
        //when
        when(treatService.getTreatById(1L)).thenReturn(dto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/treat/{id}", dto.getId()))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.amount").value(dto.getAmount()))
                .andExpect(jsonPath("$.increaseBy").value(dto.getIncreaseBy()))
                .andExpect(jsonPath("$.pic").value(dto.getPic()))
                .andExpect(jsonPath("$.userId").value(dto.getUserId()));
    }

    @Test
    public void readMany_theIdsAreInDb_200HttpResponse() throws Exception {
        //given
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        User user = ObjectFactory.User();
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> treatsDtoFromDb = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(user.getId());
            treatDto.setId((long) i + 1);
            treatDto.setName(treatNames[i]);
            treatsDtoFromDb.add(treatDto);
        }
        //when
        when(treatService.getTreatsByIds(ids)).thenReturn(treatsDtoFromDb);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/treat/many").param("ids", "1, 2, 3"))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(treatsDtoFromDb.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(treatsDtoFromDb.get(1).getId()))
                .andExpect(jsonPath("$[2].id").value(treatsDtoFromDb.get(2).getId()))
                .andExpect(jsonPath("$[0].name").value(treatsDtoFromDb.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(treatsDtoFromDb.get(1).getName()))
                .andExpect(jsonPath("$[2].name").value(treatsDtoFromDb.get(2).getName()))
                .andExpect(jsonPath("$[0].amount").value(treatsDtoFromDb.get(0).getAmount()))
                .andExpect(jsonPath("$[1].amount").value(treatsDtoFromDb.get(1).getAmount()))
                .andExpect(jsonPath("$[2].amount").value(treatsDtoFromDb.get(2).getAmount()))
                .andExpect(jsonPath("$[0].increaseBy").value(treatsDtoFromDb.get(0).getIncreaseBy()))
                .andExpect(jsonPath("$[1].increaseBy").value(treatsDtoFromDb.get(1).getIncreaseBy()))
                .andExpect(jsonPath("$[2].increaseBy").value(treatsDtoFromDb.get(2).getIncreaseBy()))
                .andExpect(jsonPath("$[0].pic").value(treatsDtoFromDb.get(0).getPic()))
                .andExpect(jsonPath("$[1].pic").value(treatsDtoFromDb.get(1).getPic()))
                .andExpect(jsonPath("$[2].pic").value(treatsDtoFromDb.get(2).getPic()))
                .andExpect(jsonPath("$[0].userId").value(treatsDtoFromDb.get(0).getUserId()))
                .andExpect(jsonPath("$[1].userId").value(treatsDtoFromDb.get(1).getUserId()))
                .andExpect(jsonPath("$[2].userId").value(treatsDtoFromDb.get(2).getUserId()));

    }

    @Test
    public void readManyByUserId_userIdIsInDb_200httpResponse() throws Exception {
        User user = ObjectFactory.User();
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> treatsDtoFromDb = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(user.getId());
            treatDto.setId((long) i + 1);
            treatDto.setName(treatNames[i]);
            treatsDtoFromDb.add(treatDto);
        }
        when(treatService.getAllTreatsByUserId(user.getId())).thenReturn(treatsDtoFromDb);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/treat/many/{userId}", user.getId()))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(treatsDtoFromDb.get(0).getUserId()))
                .andExpect(jsonPath("$[1].userId").value(treatsDtoFromDb.get(1).getUserId()))
                .andExpect(jsonPath("$[2].userId").value(treatsDtoFromDb.get(2).getUserId()));
    }

    @Test
    public void deleteById_treatIdIsNotNull_204httpResponse() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/treat/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                //then
                .andExpect(status().isNoContent());
        verify(treatService, times(1)).deleteTreatById(1L);
        verifyNoMoreInteractions(treatService);
    }

    @Test
    @Ignore
    public void deleteManyByIds_allIdsAreValid_204httpResponse() throws Exception {
        //when
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/treat/many").param("ids", "1, 2, 3"))
                //then
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteManyByUserId_userIdIsInDb_deletesAllTreatsInUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/treat/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                //then
                .andExpect(status().isNoContent());
    }
}