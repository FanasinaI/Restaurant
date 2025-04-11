package edu.hei.school.restaurant.mapper.rest;

import edu.hei.school.restaurant.dtos.responses.PriceRest;
import edu.hei.school.restaurant.model.Price;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PriceRestMapper implements Function<Price, PriceRest> {

    @Override
    public PriceRest apply(Price price) {
        return new PriceRest(price.getId(), price.getAmount(), price.getDateValue());
    }
}
