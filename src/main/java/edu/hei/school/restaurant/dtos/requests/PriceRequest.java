package edu.hei.school.restaurant.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PriceRequest {
    private Long id;
    private Double amount ;
    private LocalDateTime dateValue;
}
