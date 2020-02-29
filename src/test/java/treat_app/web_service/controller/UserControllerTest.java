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
import treat_app.web_service.exceptions.NotFoundException;
import treat_app.web_service.service.UserService;
import treat_app.web_service.service.dto.UserDto;
import treat_app.web_service.util.MyStrings;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void add_validUseWithLoginAndPassword_201httpResponse() throws Exception {
        //given
        UserDto insertDto = ObjectFactory.UserDto();
        insertDto.setId(null);
        UserDto returnedDto = ObjectFactory.UserDto();
        //when
        when(userService.createUser(insertDto)).thenReturn(returnedDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(returnedDto.getId()))
                .andExpect(jsonPath("$.userLogin").value(returnedDto.getUserLogin()))
                .andExpect(jsonPath("$.userLogin").value(returnedDto.getUserLogin()))
                .andExpect(jsonPath("$.treatDtos").value(returnedDto.getTreatDtos()));
    }

    @Test
    public void add_userIdIsNotNUll_400httpResponse() throws Exception {
        //given
        UserDto insertDto = ObjectFactory.UserDto();
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string(MyStrings.ID_ERROR, MyStrings.ID_ERROR_HAS_TO_BE_NULL));
    }

    @Test
    public void read_validIdWithNUllTreats_200httpResponse() throws Exception {
        //given
        UserDto dto = ObjectFactory.UserDto();
        //when
        when(userService.getUserByid(1L)).thenReturn(dto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(dto)))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.userLogin").value(dto.getUserLogin()))
                .andExpect(jsonPath("$.userLogin").value(dto.getUserLogin()))
                .andExpect(jsonPath("$.treatDtos").value(dto.getTreatDtos()));
    }

    @Test
    public void read_theIdIsNotInDb_404HttpResponse() throws Exception {
        //given
        UserDto dto = ObjectFactory.UserDto();
        //when
        when(userService.getUserByid(1L)).thenThrow(NotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", dto.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(dto)))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_userIdIsNotNull_200httpResponse() throws Exception {
        //given
        UserDto insertDto = ObjectFactory.UserDto();
        UserDto returnedDto = ObjectFactory.UserDto();
        //when
        when(userService.updateUser(insertDto)).thenReturn(returnedDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(returnedDto.getId()))
                .andExpect(jsonPath("$.userLogin").value(returnedDto.getUserLogin()))
                .andExpect(jsonPath("$.userLogin").value(returnedDto.getUserLogin()))
                .andExpect(jsonPath("$.treatDtos").value(returnedDto.getTreatDtos()));
    }

    @Test
    public void update_userIdIsNull_400httpResponse() throws Exception {
        //given
        UserDto insertDto = ObjectFactory.UserDto();
        insertDto.setId(null);
        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string(MyStrings.ID_ERROR, MyStrings.ID_ERROR_CANNOT_BE_NULL));
    }

    @Test
    public void deleteById_UserIdIsNotNull_204httpResponse() throws Exception {
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                //then
                .andExpect(status().isNoContent());
    }
}