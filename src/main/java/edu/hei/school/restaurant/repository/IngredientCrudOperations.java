package edu.hei.school.restaurant.repository;

import edu.hei.school.restaurant.Configuration.DataSource;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.mapper.IngredientMapper;
import edu.hei.school.restaurant.exception.NotFoundException;
import edu.hei.school.restaurant.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientCrudOperations implements CrudOperations<Ingredient> {
    private final DataSource dataSource;
    private final IngredientMapper ingredientMapper;
    private final PriceCrudOperations priceCrudOperations;
    private final StockMovementCrudOperations stockMovementCrudOperations;

    @Override
    public List<Ingredient> getAll(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select i.id, i.name from ingredient i order by i.id asc limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Ingredient ingredient = ingredientMapper.apply(resultSet);
                    // charger les prix et les stocks
                    ingredient.getPrices().addAll(priceCrudOperations.findByIdIngredient(ingredient.getId()));
                    ingredient.getStockMovements().addAll(stockMovementCrudOperations.findByIdIngredient(ingredient.getId()));
                    ingredients.add(ingredient);
                }
                return ingredients;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public Ingredient findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select i.id, i.name from ingredient i where i.id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Ingredient ingredient = ingredientMapper.apply(resultSet);
                    ingredient.getPrices().addAll(priceCrudOperations.findByIdIngredient(ingredient.getId()));
                    ingredient.getStockMovements().addAll(stockMovementCrudOperations.findByIdIngredient(ingredient.getId()));
                    return ingredient;
                }
                throw new NotFoundException("Ingredient.id=" + id + " not found");
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }


    @SneakyThrows
    @Override
    public List<Ingredient> saveAll(List<Ingredient> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Entities list cannot be null");
        }

        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "insert into ingredient (id, name) values (?, ?)"
                + " on conflict (id) do update set name=excluded.name"
                + " returning id, name";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Ingredient entityToSave : entities) {
                if (entityToSave == null) {
                    continue;
                }

                statement.setLong(1, entityToSave.getId());
                statement.setString(2, entityToSave.getName());

                // Execute the statement and get the result
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        ingredients.add(ingredientMapper.apply(resultSet));
                    }
                }

                // Save related entities
                if (entityToSave.getPrices() != null) {
                    priceCrudOperations.saveAll(entityToSave.getPrices());
                }
                if (entityToSave.getStockMovements() != null) {
                    stockMovementCrudOperations.saveAll(entityToSave.getStockMovements());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving ingredients", e);
        }

        return ingredients;
    }
}
