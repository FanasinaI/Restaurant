package edu.hei.school.restaurant.repository;

import edu.hei.school.restaurant.Configuration.DataSource;
import edu.hei.school.restaurant.mapper.PriceMapper;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.exception.ServerException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PriceCrudOperations implements CrudOperations<Price> {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PriceMapper priceMapper;

    @Override
    public List<Price> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Price findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SneakyThrows
    @Override
    public List<Price> saveAll(List<Price> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Entities list cannot be null");
        }

        List<Price> prices = new ArrayList<>();
        String sql = "insert into price (id, amount, date_value, id_ingredient) values (?, ?, ?, ?)"
                + " on conflict (id) do update set amount=excluded.amount, date_value=excluded.date_value, id_ingredient=excluded.id_ingredient"
                + " returning id, amount, date_value, id_ingredient";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Price entityToSave : entities) {
                if (entityToSave == null) {
                    continue;
                }

                statement.setLong(1, entityToSave.getId());
                statement.setDouble(2, entityToSave.getAmount());
                statement.setDate(3, Date.valueOf(entityToSave.getDateValue()));
                statement.setLong(4, entityToSave.getIngredient().getId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        prices.add(priceMapper.apply(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving prices", e);
        }

        return prices;
    }


    public List<Price> findByIdIngredient(Long idIngredient) {
        List<Price> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select p.id, p.amount, p.date_value from price p"
                     + " join ingredient i on p.id_ingredient = i.id"
                     + " where p.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Price price = priceMapper.apply(resultSet);
                    prices.add(price);
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
