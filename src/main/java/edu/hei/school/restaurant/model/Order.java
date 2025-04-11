package edu.hei.school.restaurant.model;

import edu.hei.school.restaurant.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {
    private String reference;
    private List<DishOrder> dishes;
    private OrderStatus status;
    private Double totalPrice;
}

