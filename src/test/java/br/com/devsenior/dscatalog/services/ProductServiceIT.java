package br.com.devsenior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import br.com.devsenior.dscatalog.dto.ProductDTO;
import br.com.devsenior.dscatalog.repositories.ProductRepository;
import br.com.devsenior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest // Carregando todo contexto da aplicação
@Transactional // Para reiniciar os elementos da base de dados a cada teste
public class ProductServiceIT {

    @Autowired // Ou seja, o service não está mockado..
    private ProductService service;

    @Autowired // Chamando repository para teste de integração real.
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);

        // Após excluir um produto, verifique se agora tem a quantidade esperada 25 -1 = 24
        Assertions.assertEquals(countTotalProducts - 1, repository.count());

    }

    @Test
    public void deleteShouldThrowResourceNotFoundExeptionWhenIdDoesNotExists() {
        
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistId);
        });

    }

    @Test
    public void findAllShouldReturnPageWhenPage0size10() {
        //Classe que implementa o page
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllShouldReturnEmptyPageWhenPageDoesNotExist() {
        //Classe que implementa o page
        // É ára falhar, pois só temos 25 elementos na base de dados e o pageNumber está 50
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = service.findAll(pageRequest);

        Assertions.assertTrue(result.isEmpty());
 
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() {
        //Classe que implementa o page
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = service.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());

        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
 
    }
    
}
