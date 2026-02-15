package fr.forty_two.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import fr.forty_two.models.Product;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {
    private final DataSource engine;

    public ProductsRepositoryJdbcImpl(DataSource engine) {
        this.engine = engine;
    }

    @Override
    public List<Product> findAll() {
        final String sql = "SELECT * FROM product";

        try (Connection conn = engine.getConnection();
            Statement ps = conn.createStatement()) {

            try (ResultSet rs = ps.executeQuery(sql)) {
                List<Product> products = new ArrayList<>();
                
                while (rs.next()) {
                    products.add(new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price")
                    ));
                }
                return products;
            }
        } catch (SQLException e) {
            System.err.print("SQLERR: ");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        final String sql = "SELECT name, price FROM product WHERE id = ?";

        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product(
                        id, 
                        rs.getString("name"),
                        rs.getBigDecimal("price")
                    );
                    return Optional.of(p);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.err.print("SQLERR: ");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void save(Product product) {
        final String sql = "INSERT INTO product (name, price) VALUES(?, ?)";

        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getName());
            ps.setBigDecimal(2, product.getPrice());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.print("SQLERR: ");
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        final String sql = "UPDATE product SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setBigDecimal(2, product.getPrice());
            ps.setLong(3, product.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.print("SQLERR: ");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        final String sql = "DELETE FROM product WHERE id = ?";

        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.print("SQLERR: ");
            e.printStackTrace();
        }
    }
}
