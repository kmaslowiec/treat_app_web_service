package treat_app.web_service.controller;

import org.springframework.http.HttpHeaders;
import treat_app.web_service.util.MyStrings;

public class HeaderFactory {

    public static HttpHeaders idHasToBeNull(Long id) {
        HttpHeaders head = new HttpHeaders();
        head.add(MyStrings.ID_ERROR, MyStrings.ID_ERROR_HAS_TO_BE_NULL);
        return head;
    }

    public static HttpHeaders idCantBeNull(Long id) {
        HttpHeaders head = new HttpHeaders();
        head.add(MyStrings.ID_ERROR, MyStrings.ID_ERROR_CANNOT_BE_NULL);
        return head;
    }
}