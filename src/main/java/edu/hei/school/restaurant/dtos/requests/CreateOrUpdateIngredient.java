package edu.hei.school.restaurant.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateOrUpdateIngredient {
    private Long id;
    private String name;
}
