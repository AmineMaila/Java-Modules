package fr.forty_two.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import fr.forty_two.models.Product;

public class ProductsRepositoryJdbcImplTest {
    private EmbeddedDatabase ds;
    private ProductsRepositoryJdbcImpl productRepo;
    private final List<Product> EXPECTED_FIND_ALL_PRODUCTS = List.of(
        new Product(0L, "book", BigDecimal.valueOf(11.99)),
        new Product(1L, "elden ring", BigDecimal.valueOf(59.99)),
        new Product(2L, "computer", BigDecimal.valueOf(749.99)),
        new Product(3L, "rubix cube", BigDecimal.valueOf(4.99)),
        new Product(4L, "bike", BigDecimal.valueOf(999.99))
    );
    private final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(2L, "computer", BigDecimal.valueOf(749.99));
    private final Product EXPECTED_UPDATED_PRODUCT = new Product(2L, "gaming computer", BigDecimal.valueOf(1249.89));
    private final Product EXPECTED_SAVE_PRODUCT = new Product(5L, "house", BigDecimal.valueOf(412_000.00));


    @BeforeEach
    public void init() {
        ds = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .addScript("data.sql")
            .build();
        productRepo = new ProductsRepositoryJdbcImpl(ds);
    }

    @AfterEach
    public void reset() {
        this.ds.shutdown();
    }

    @Test
    void shouldReturnProductWhenProductIdExists() {
        Optional<Product> p = productRepo.findById(2L);

        assertTrue(p.isPresent());
        assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, p.get());
    }

    @Test
    void shouldReturnAllExistingProducts() {
        List<Product> products = productRepo.findAll();

        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, products);
    }

    @Test
    void shouldUpdateExistingProduct() {
        Product input = new Product(2L, "gaming computer", BigDecimal.valueOf(1249.89));
        productRepo.update(input);
        Optional<Product> result = productRepo.findById(2L);
        
        assertTrue(result.isPresent());
        assertEquals(EXPECTED_UPDATED_PRODUCT, result.get());
    }

    @Test
    void shouldSaveToDatabaseWhenValidData() {
        Product input = new Product(null, "house", BigDecimal.valueOf(412_000.00));
        productRepo.save(input);
        Optional<Product> result = productRepo.findById(input.getId());
        assertTrue(result.isPresent());
        assertEquals(EXPECTED_SAVE_PRODUCT, result.get());
    }
}
