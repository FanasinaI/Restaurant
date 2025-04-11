package edu.hei.school.restaurant.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreatePrice {
    private Long id;
    private Long idIngredient;
    private Double amount ;
    private LocalDateTime dateValue;
}
