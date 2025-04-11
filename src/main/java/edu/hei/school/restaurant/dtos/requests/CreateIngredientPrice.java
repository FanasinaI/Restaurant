package edu.hei.school.restaurant.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CreateIngredientPrice {
    private Double amount;
    private LocalDate dateValue;
}
