package br.com.devsenior.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.devsenior.dscatalog.dto.CategoryDTO;
import br.com.devsenior.dscatalog.dto.ProductDTO;
import br.com.devsenior.dscatalog.entities.Category;
import br.com.devsenior.dscatalog.entities.Product;
import br.com.devsenior.dscatalog.repositories.CategoryRepository;
import br.com.devsenior.dscatalog.repositories.ProductRepository;
import br.com.devsenior.dscatalog.services.exceptions.DataBaseException;
import br.com.devsenior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService implements IProductService {
    
    @Autowired
    private ProductRepository repository;
    
    @Autowired
    private CategoryRepository CategoryRepository;

    @Transactional(readOnly = true)
    @Override
    public ProductDTO findById(Long id) {
        Product entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable);
        return result.map(x -> new ProductDTO(x));
    }

    @Transactional
    @Override
    public ProductDTO save(ProductDTO dto) {
        Product entity = new Product();
        dtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    
    @Transactional
    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado!" + id);
        }
        Product entity = repository.getReferenceById(id);
        dtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Override
    public void delete(Long id) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Falha de violação de integridade");
        }
    }

    private void dtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = CategoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }
    }

    


}
