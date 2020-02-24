package treat_app.web_service.controller;

import org.springframework.http.HttpHeaders;
import treat_app.web_service.util.MyStrings;

public class HeaderFactory {

    public static HttpHeaders idHasToBeNull() {
        HttpHeaders head = new HttpHeaders();
        head.add(MyStrings.ID_ERROR, MyStrings.ID_ERROR_HAS_TO_BE_NULL);
        return head;
    }

    public static HttpHeaders idCantBeNull() {
        HttpHeaders head = new HttpHeaders();
        head.add(MyStrings.ID_ERROR, MyStrings.ID_ERROR_CANNOT_BE_NULL);
        return head;
    }

    public static HttpHeaders UserIdCantBeNull() {
        HttpHeaders head = new HttpHeaders();
        head.add(MyStrings.TREAT_ID_ERROR, MyStrings.TREAT_USER_ID_ERROR_CANNOT_BE_NULL);
        return head;
    }

    public static HttpHeaders TreatsCannotBeEmpty() {
        HttpHeaders head = new HttpHeaders();
        head.add(MyStrings.TREAT_LIST_ERROR, MyStrings.TREAT_LIST_ERROR_LIST_CANNOT_BE_EMPTY);
        return head;
    }
}