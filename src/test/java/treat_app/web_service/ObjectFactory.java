package treat_app.web_service;

import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.dto.UserDto;

public class ObjectFactory {

    private static final Long ID = 1L;
    private static final String PASS = "xxx";
    private static final String LOGIN = "konrad";
    private static final String TREAD_NAME = "lust";
    private static final Float AMOUNT = 5.0f;
    private static final Float INC_BY = 2.5f;
    private static final int PIC = 1;

    // User
    public static User User() {
        return User.builder().id(ID).userLogin(LOGIN).password(PASS).build();
    }

    public static User User_id_login(Long id, String login) {
        return User.builder().id(id).userLogin(login).password(PASS).build();
    }

    //UserDto
    public static UserDto UserDto() {
        return UserDto.builder().id(ID).userLogin(LOGIN).password(PASS).build();
    }

    public static UserDto UserDto_id_login(Long id, String login) {
        return UserDto.builder().id(id).userLogin(login).password(PASS).build();
    }

    //Treat
    public static Treat Treat_user(User user) {
        return Treat.builder().id(ID).name(TREAD_NAME).amount(AMOUNT).increaseBy(INC_BY).pic(PIC).user(user).build();
    }

    public static Treat Treat_id_name_user(Long id, String name, User user) {
        return Treat.builder().id(id).name(name).amount(AMOUNT).increaseBy(INC_BY).pic(PIC).user(user).build();
    }

    //TreatDto
    public static TreatDto TreatDto_userId(Long userId) {
        return TreatDto.builder().id(ID).name(TREAD_NAME).amount(AMOUNT).increaseBy(INC_BY).pic(PIC).userId(userId).build();
    }

    public static TreatDto TreatDto_id_name_userId(Long id, String name, Long userId) {
        return TreatDto.builder().id(id).name(name).amount(AMOUNT).increaseBy(INC_BY).pic(PIC).userId(userId).build();
    }
}
