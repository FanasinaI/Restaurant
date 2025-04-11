package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dtos.requests.CreateStockMovement;
import edu.hei.school.restaurant.dtos.requests.StockMovementRequest;
import edu.hei.school.restaurant.model.StockMovement;
import edu.hei.school.restaurant.repository.StockMovementCrudOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementCrudOperations stockMovementCrudOperations;

    public List<StockMovement> getStockMovementsByIngredientId(Long ingredientId) {
        return stockMovementCrudOperations.findByIdIngredient(ingredientId);
    }

    public List<StockMovement> saveAllStockMovements(List<StockMovement> stockMovements) {
        return stockMovementCrudOperations.saveAll(stockMovements);
    }

    public StockMovement findStockMovementById(Long id) {
        return stockMovementCrudOperations.findById(id);
    }

    public List<CreateStockMovement> createStockMovementsFromRequests(Long idIngredient, List<StockMovementRequest> stockMovementRequests) {
        return stockMovementRequests.stream()
                .map(request -> new CreateStockMovement(request.getId(), idIngredient, request.getQuantity(), request.getUnit(), request.getMovementType(), request.getCreationDatetime()))
                .collect(Collectors.toList());
    }
}
