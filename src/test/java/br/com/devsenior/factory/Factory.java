package br.com.devsenior.factory;

import java.time.Instant;

import br.com.devsenior.dscatalog.dto.ProductDTO;
import br.com.devsenior.dscatalog.entities.Category;
import br.com.devsenior.dscatalog.entities.Product;

public class Factory {
    
    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2024-09-04T10:00:00Z"));
        product.getCategories().add(new Category(1L, "Electronics"));
        return product;
    }

    public static ProductDTO creaProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
