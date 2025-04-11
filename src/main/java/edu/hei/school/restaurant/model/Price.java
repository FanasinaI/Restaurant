package edu.hei.school.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static java.time.LocalDate.now;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Price {
    private Long id;
    private Ingredient ingredient;
    private Double amount;
    private LocalDate dateValue;

    // Constructor with ID, amount, and dateValue
    public Price(Long id, Double amount, LocalDate dateValue) {
        this.id = id;
        this.amount = amount;
        this.dateValue = dateValue;
    }

    // Constructor with amount and dateValue
    public Price(Double amount, LocalDate dateValue) {
        this.amount = amount;
        this.dateValue = dateValue;
    }

    // Constructor with only amount, defaulting dateValue to now
    public Price(Double amount) {
        this.amount = amount;
        this.dateValue = now();
    }
}
