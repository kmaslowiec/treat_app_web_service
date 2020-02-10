package treat_app.web_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
