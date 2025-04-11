package edu.hei.school.restaurant.dtos.responses;

import edu.hei.school.restaurant.enums.StockMovementType;
import edu.hei.school.restaurant.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class StockMovementRest {
    private Long id;
    private Double quantity;
    private Unit unit;
    private StockMovementType type;
    private Instant creationDatetime;
}
