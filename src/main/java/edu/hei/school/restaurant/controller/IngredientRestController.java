package edu.hei.school.restaurant.controller;

import edu.hei.school.restaurant.dtos.requests.*;
import edu.hei.school.restaurant.mapper.rest.IngredientRestMapper;
import edu.hei.school.restaurant.dtos.responses.IngredientRest;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.model.StockMovement;
import edu.hei.school.restaurant.service.IngredientService;
import edu.hei.school.restaurant.exception.ClientException;
import edu.hei.school.restaurant.exception.NotFoundException;
import edu.hei.school.restaurant.exception.ServerException;
import edu.hei.school.restaurant.service.PriceService;
import edu.hei.school.restaurant.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class IngredientRestController {
    private final IngredientService ingredientService;
    private final IngredientRestMapper ingredientRestMapper;

    @Autowired
    private final PriceService priceService;
    @Autowired
    private final StockMovementService stockMovementService;

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
                                                 @RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {
        try {
            List<Ingredient> ingredientsByPrices = ingredientService.getIngredientsByPrices(priceMinFilter, priceMaxFilter);
            List<IngredientRest> ingredientRests = ingredientsByPrices.stream()
                    .map(ingredient -> ingredientRestMapper.toRest(ingredient))
                    .toList();
            return ResponseEntity.ok().body(ingredientRests);
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping("/ingredients")
    public ResponseEntity<Object> addIngredients(@RequestBody List<CreateOrUpdateIngredient> ingredientsToCreate) {
        try {
            List<Ingredient> ingredients = ingredientsToCreate.stream()
                    .map(ingredientRestMapper::toModel)
                    .collect(Collectors.toList());

            List<IngredientRest> ingredientsRest = ingredientService.saveAll(ingredients).stream()
                    .map(ingredientRestMapper::toRest)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ingredientsRest);
        } catch (ServerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/ingredients")
    public ResponseEntity<Object> updateIngredients(@RequestBody List<CreateOrUpdateIngredient> ingredientsToCreateOrUpdate) {
        try {
            List<Ingredient> ingredients = ingredientsToCreateOrUpdate.stream()
                    .map(ingredient -> ingredientRestMapper.toModel(ingredient))
                    .toList();
            List<IngredientRest> ingredientsRest = ingredientService.saveAll(ingredients).stream()
                    .map(ingredient -> ingredientRestMapper.toRest(ingredient))
                    .toList();
            return ResponseEntity.ok().body(ingredientsRest);
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/ingredients/{ingredientId}/prices")
    public ResponseEntity<Object> updateIngredientPrices(
            @PathVariable Long ingredientId,
            @RequestBody List<PriceRequest> priceRequests) {
        for (PriceRequest request : priceRequests) {
            if (request.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Price ID cannot be null");
            }
        }
        Ingredient ingredient = ingredientService.getById(ingredientId);
        if (ingredient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient not found");
        }
        List<CreatePrice> createPrices = priceService.createPricesFromRequests(ingredientId, priceRequests);
        List<Price> prices = createPrices.stream()
                .map(cp -> {
                    Price price = new Price(cp.getId(), cp.getAmount(), cp.getDateValue().toLocalDate());
                    price.setIngredient(ingredient);
                    return price;
                })
                .collect(Collectors.toList());
        System.out.println(
                prices
        );
        priceService.saveAllPrices(prices);
        Ingredient updatedIngredient = ingredientService.getById(ingredientId);
        IngredientRest ingredientRest = ingredientRestMapper.toRest(updatedIngredient);

        return ResponseEntity.ok(ingredientRest);
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredient(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(ingredientRestMapper.toRest(ingredientService.getById(id)));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/ingredients/{ingredientId}/stockMovements")
    public ResponseEntity<Object> updateIngredientStockMovements(
            @PathVariable Long ingredientId,
            @RequestBody List<StockMovementRequest> stockMovementRequests) {

        for (StockMovementRequest request : stockMovementRequests) {
            if (request.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("StockMovement ID cannot be null");
            }
        }

        Ingredient ingredient = ingredientService.getById(ingredientId);
        if (ingredient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient not found");
        }

        List<CreateStockMovement> createStockMovements = stockMovementService.createStockMovementsFromRequests(ingredientId, stockMovementRequests);
        List<StockMovement> stockMovements = createStockMovements.stream()
                .map(csm -> {
                    StockMovement stockMovement = new StockMovement(csm.getId(), ingredient, csm.getQuantity(), csm.getUnit(), csm.getMovementType(), csm.getCreationDatetime());
                    return stockMovement;
                })
                .collect(Collectors.toList());

        stockMovementService.saveAllStockMovements(stockMovements);

        Ingredient updatedIngredient = ingredientService.getById(ingredientId);
        IngredientRest ingredientRest = ingredientRestMapper.toRest(updatedIngredient);

        return ResponseEntity.ok(ingredientRest);
    }
}
