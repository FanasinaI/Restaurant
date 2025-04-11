package edu.hei.school.restaurant.mapper.rest;

import edu.hei.school.restaurant.dtos.responses.StockMovementRest;
import edu.hei.school.restaurant.model.StockMovement;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class StockMovementRestMapper implements Function<StockMovement, StockMovementRest> {

    @Override
    public StockMovementRest apply(StockMovement stockMovement) {
        return new StockMovementRest(stockMovement.getId(),
                stockMovement.getQuantity(),
                stockMovement.getUnit(),
                stockMovement.getMovementType(),
                stockMovement.getCreationDatetime());
    }
}
