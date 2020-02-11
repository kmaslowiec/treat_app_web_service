package treat_app.web_service.controller;

import org.springframework.http.HttpHeaders;

public class HeaderFactory {

    public static HttpHeaders idNotNUll(Long id) {
        HttpHeaders head = new HttpHeaders();
        head.add("id-error", String.format("The id has to be null and it is %d", id));
        return head;
    }
}