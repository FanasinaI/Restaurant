package edu.hei.school.restaurant.dtos.requests;

import edu.hei.school.restaurant.enums.StockMovementType;
import edu.hei.school.restaurant.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class StockMovementRequest {
    private Long id;
    private Double quantity;
    private Unit unit;
    private StockMovementType movementType;
    private Instant creationDatetime;
}
