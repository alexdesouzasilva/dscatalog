package br.com.devsenior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.devsenior.dscatalog.entities.Product;
import br.com.devsenior.factory.Factory;

@DataJpaTest // Carrega apenas o contexto de JPA.
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long notExistingId;
    private long countTotalProducts;

    @BeforeEach // antes de cada teste
    void setUp() throws Exception {
        existingId = 1L;
        notExistingId = 1000L;
        countTotalProducts = 26L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        //Prepara os dados
        Product product = Factory.createProduct();
        product.setId(null);
        //Excecuta ação
        product = repository.save(product);
        //Verifica resultado
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(26, product.getId());
    }

    @Test
    public void findByIdShouldReturnsOptionalNotEmptyWhenIdExist() {
        //Preparar dados + ação
        Optional<Product> result = repository.findById(existingId);
        //Verifica resultado
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldRetrunsOptionalEmptyWhenIdNotExist() {
        //Preparar dados + ação
        Optional<Product> result = repository.findById(notExistingId);
        //Verifica resultado
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        // Executa ação
        repository.deleteById(existingId);
        //Verifica resultado
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }
    
}
