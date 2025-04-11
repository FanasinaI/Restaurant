package edu.hei.school.restaurant.repository;

import edu.hei.school.restaurant.Configuration.DataSource;
import edu.hei.school.restaurant.mapper.StockMovementMapper;
import edu.hei.school.restaurant.model.StockMovement;
import edu.hei.school.restaurant.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

@Repository
public class StockMovementCrudOperations implements CrudOperations<StockMovement> {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private StockMovementMapper stockMovementMapper;

    @Override
    public List<StockMovement> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StockMovement findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StockMovement> saveAll(List<StockMovement> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Entities list cannot be null");
        }

        List<StockMovement> stockMovements = new ArrayList<>();
        String sql = """
            insert into stock_movement (id, quantity, unit, movement_type, creation_datetime, id_ingredient)
            values (?, ?, ?, ?, ?, ?)
            on conflict (id) do update set
                quantity = excluded.quantity,
                unit = excluded.unit,
                movement_type = excluded.movement_type,
                creation_datetime = excluded.creation_datetime,
                id_ingredient = excluded.id_ingredient
            returning id, quantity, unit, movement_type, creation_datetime, id_ingredient""";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (StockMovement entityToSave : entities) {
                if (entityToSave == null) {
                    continue;
                }

                statement.setLong(1, entityToSave.getId());
                statement.setDouble(2, entityToSave.getQuantity());
                statement.setString(3, entityToSave.getUnit().name());
                statement.setString(4, entityToSave.getMovementType().name());
                statement.setTimestamp(5, Timestamp.from(entityToSave.getCreationDatetime()));
                statement.setLong(6, entityToSave.getIngredient().getId());
                statement.addBatch();
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(stockMovementMapper.apply(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving stock movements", e);
        }

        return stockMovements;
    }

    public List<StockMovement> findByIdIngredient(Long idIngredient) {
        List<StockMovement> stockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select s.id, s.quantity, s.unit, s.movement_type, s.creation_datetime from stock_movement s"
                             + " join ingredient i on s.id_ingredient = i.id"
                             + " where s.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(stockMovementMapper.apply(resultSet));
                }
                return stockMovements;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
