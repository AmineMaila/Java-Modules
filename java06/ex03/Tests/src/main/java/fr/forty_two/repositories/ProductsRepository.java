package fr.forty_two.repositories;

import java.util.List;
import java.util.Optional;

import fr.forty_two.models.Product;

public interface ProductsRepository {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    void update(Product product);
    void save(Product product);
    void delete(Long id);
}
