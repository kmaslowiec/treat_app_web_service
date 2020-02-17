package treat_app.web_service.controller;

import org.springframework.http.HttpHeaders;
import treat_app.web_service.util.MyStrings;

public class HeaderFactory {

    public static HttpHeaders idNotNull(Long id) {
        HttpHeaders head = new HttpHeaders();
        head.add(MyStrings.ID_ERROR, MyStrings.ID_ERROR_NOT_NULL);
        return head;
    }

    public static HttpHeaders idIsNull(Long id) {
        HttpHeaders head = new HttpHeaders();
        head.add(MyStrings.ID_ERROR, MyStrings.ID_ERROR_NULL);
        return head;
    }
}