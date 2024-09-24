package br.com.devsenior.dscatalog.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.devsenior.dscatalog.dto.ProductDTO;
import br.com.devsenior.dscatalog.entities.Category;
import br.com.devsenior.dscatalog.entities.Product;
import br.com.devsenior.dscatalog.repositories.CategoryRepository;
import br.com.devsenior.dscatalog.repositories.ProductRepository;
import br.com.devsenior.dscatalog.services.exceptions.DataBaseException;
import br.com.devsenior.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.devsenior.factory.Factory;
import br.com.devsenior.factory.FactoryCategory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        productDTO = Factory.creaProductDTO();
        category = FactoryCategory.createCategory();

        //Comportamento simulado.
        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        //Comportamento simulado. Quando eu passar um id que existe para o método deleteById, não deverá fazer nada
        Mockito.doNothing().when(repository).deleteById(existingId);

        Mockito.doThrow(DataIntegrityViolationException.class)
        .when(repository).deleteById(dependentId);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
    }

   @Test
    public void updateShouldReturnProductDtoWhenExistId() {
        ProductDTO dto = service.update(existingId, productDTO);
        Assertions.assertNotNull(dto);

        Mockito.verify(repository, times(1)).existsById(existingId);
        Mockito.verify(repository, times(1)).getReferenceById(existingId);
        Mockito.verify(categoryRepository, times(1)).getReferenceById(existingId);
        Mockito.verify(repository, times(1)).save(product);

    }

    @Test
    public void updateShouldReturnResourceNotFoundWhenNotExistId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDTO);
        });

        Mockito.verify(repository, times(1)).existsById(nonExistingId);
    }


    @Test
    public void findByIdShouldReturnProductDtoWhenExistId() {
        ProductDTO dto = service.findById(existingId);
        Assertions.assertNotNull(dto);

        Mockito.verify(repository, times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundWhenNotExistId() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

        Mockito.verify(repository, times(1)).findById(nonExistingId);
    }

    @Test
    public void findAllShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }


    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        // Não haverá exceção quando eu chamar com o ID que existe
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId); // chama o deleteById do mokito
        });

        //Verifica se o teste do service acima, chamou o método da interface repository
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
        //times -> validar quantas vezes o método deleteById foi chamado
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            service.delete(dependentId);
        });
    }
    
}
