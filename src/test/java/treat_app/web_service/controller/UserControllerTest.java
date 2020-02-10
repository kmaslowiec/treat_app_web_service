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
import treat_app.web_service.service.UserService;
import treat_app.web_service.service.dto.UserDto;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void addUser() {
    }

    @Test
    public void read_validIdWithNUllTreats_200httpsResponse() throws Exception {
        //given
        UserDto dto = ObjectFactory.UserDto();
        //when
        when(userService.getByid(1L)).thenReturn(dto);
        System.out.print(Converter.asJsonString(dto));
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
    public void readTest() {
    }
}
