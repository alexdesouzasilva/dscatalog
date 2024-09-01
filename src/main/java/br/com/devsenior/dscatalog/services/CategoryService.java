package br.com.devsenior.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.devsenior.dscatalog.dto.CategoryDTO;
import br.com.devsenior.dscatalog.entities.Category;
import br.com.devsenior.dscatalog.repositories.CategoryRepository;
import br.com.devsenior.dscatalog.services.exceptions.DataBaseException;
import br.com.devsenior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    @Override
    public CategoryDTO findById(Long id) {
        Category entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!"));
        return new CategoryDTO(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> result = repository.findAll(pageable);
        return result.map(x -> new CategoryDTO(x));
    }

    @Transactional
    @Override
    public CategoryDTO save(CategoryDTO dto) {
        Category entity = new Category();
        dtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    @Override
    public CategoryDTO update(Long id, CategoryDTO dto) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado!");
        }
        Category entity = repository.getReferenceById(id);
        dtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new CategoryDTO(entity);

    }

    @Transactional
    @Override
    public void delete(Long id) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch(DataIntegrityViolationException e) {
            throw new DataBaseException("Falha de integridade na base de dados");
        }
    }

    private void dtoToEntity(CategoryDTO dto, Category entity) {
        entity.setName(dto.getName());
    }
    
}
