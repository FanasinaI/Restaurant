package edu.hei.school.restaurant.model;


import edu.hei.school.restaurant.dtos.responses.DishRest;
import edu.hei.school.restaurant.enums.DishOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DishOrder {
    private Long id;
    private DishRest dish;
    private Integer quantity;
    private DishOrderStatus status;
}