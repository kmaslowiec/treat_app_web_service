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
import treat_app.web_service.exceptions.NotFoundException;
import treat_app.web_service.service.UserService;
import treat_app.web_service.service.dto.UserDto;

import static org.mockito.Mockito.doThrow;
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
    public void addUser_validUseWithLoginAndPassword_201httpsResponse() throws Exception {
        //given
        UserDto insertDto = ObjectFactory.UserDto();
        insertDto.setId(null);
        UserDto returnedDto = ObjectFactory.UserDto();
        //when
        when(userService.create(insertDto)).thenReturn(returnedDto);
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
    public void addUser_userIdIsNotNUll_400httpsResponse() throws Exception {
        //given
        UserDto insertDto = ObjectFactory.UserDto();
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string("id-error", "The id has to be null and it is " + insertDto.getId()));
    }

    @Test
    public void read_validIdWithNUllTreats_200httpsResponse() throws Exception {
        //given
        UserDto dto = ObjectFactory.UserDto();
        //when
        when(userService.getByid(1L)).thenReturn(dto);
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
    public void read_notExistingId_404httpsResponse() throws Exception {
        //given
        UserDto dto = ObjectFactory.UserDto();
        //when
        doThrow(new NotFoundException("")).when(userService).getByid(dto.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(dto)))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @Ignore
    public void updateUser_userIdIsNotNull_200httpsResponse() throws Exception {
        //given
        UserDto insertDto = ObjectFactory.UserDto();
        UserDto returnedDto = ObjectFactory.UserDto();
        //when
        when(userService.update(insertDto)).thenReturn(returnedDto);
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
    @Ignore
    public void updateUser_userIdIsNull_400httpsResponse() throws Exception {
        //given
        UserDto insertDto = ObjectFactory.UserDto();
        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(Converter.asJsonString(insertDto)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(header().string("id-error", "The id cannot be null"));
    }
}