package treat_app.web_service.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TreatDto {

    private Long id;
    private String name;
    private Float amount;
    private Float increaseBy;
    private int pic;
    private Long userId;
}