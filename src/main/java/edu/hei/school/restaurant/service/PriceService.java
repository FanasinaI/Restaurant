package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dtos.requests.CreatePrice;
import edu.hei.school.restaurant.dtos.requests.PriceRequest;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.repository.PriceCrudOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceCrudOperations priceCrudOperations;

    public List<Price> getPricesByIngredientId(Long ingredientId) {
        return priceCrudOperations.findByIdIngredient(ingredientId);
    }

    public List<Price> saveAllPrices(List<Price> prices) {
        return priceCrudOperations.saveAll(prices);
    }

    public Price findPriceById(Long id) {
        return priceCrudOperations.findById(id);
    }
    public List<CreatePrice> createPricesFromRequests(Long idIngredient, List<PriceRequest> priceRequests) {
        return priceRequests.stream()
                .map(priceRequest -> new CreatePrice(priceRequest.getId(), idIngredient, priceRequest.getAmount(), priceRequest.getDateValue()))
                .collect(Collectors.toList());
    }


}
