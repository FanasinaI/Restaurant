package edu.hei.school.restaurant.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class PriceRest {
    private Long id;
    private Double price;
    private LocalDate dateValue;
}
