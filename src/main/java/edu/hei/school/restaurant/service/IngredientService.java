package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.repository.IngredientCrudOperations;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientCrudOperations ingredientCrudOperations;

    public List<Ingredient> getIngredientsByPrices(Double priceMinFilter, Double priceMaxFilter) {
        if (priceMinFilter != null && priceMinFilter < 0) {
            throw new ClientException("PriceMinFilter " + priceMinFilter + " is negative");
        }
        if (priceMaxFilter != null && priceMaxFilter < 0) {
            throw new ClientException("PriceMaxFilter " + priceMaxFilter + " is negative");
        }
        if (priceMinFilter != null && priceMaxFilter != null) {
            if (priceMinFilter > priceMaxFilter) {
                throw new ClientException("PriceMinFilter " + priceMinFilter + " is greater than PriceMaxFilter " + priceMaxFilter);
            }
        }
        // TODO : paginate from restController OR filter from repository directly
        List<Ingredient> ingredients = ingredientCrudOperations.getAll(1, 500);

        return ingredients.stream()
                .filter(ingredient -> {
                    if (priceMinFilter == null && priceMaxFilter == null) {
                        return true;
                    }
                    Double unitPrice = ingredient.getActualPrice();
                    if (priceMinFilter != null && priceMaxFilter == null) {
                        return unitPrice >= priceMinFilter;
                    }
                    if (priceMinFilter == null) {
                        return unitPrice <= priceMaxFilter;
                    }
                    return unitPrice >= priceMinFilter && unitPrice <= priceMaxFilter;
                })
                .toList();
    }

    public List<Ingredient> getAll(Integer page, Integer size) {
        return ingredientCrudOperations.getAll(page, size);
    }

    public Ingredient getById(Long id) {
        return ingredientCrudOperations.findById(id);
    }

    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
        return ingredientCrudOperations.saveAll(ingredients);
    }

    public Ingredient addPrices(Long ingredientId, List<Price> pricesToAdd) {
        Ingredient ingredient = ingredientCrudOperations.findById(ingredientId);
        ingredient.addPrices(pricesToAdd);
        List<Ingredient> ingredientsSaved = ingredientCrudOperations.saveAll(List.of(ingredient));
        return ingredientsSaved.getFirst();
    }
}
