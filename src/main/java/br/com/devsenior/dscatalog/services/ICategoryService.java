package br.com.devsenior.dscatalog.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.devsenior.dscatalog.dto.CategoryDTO;

public interface ICategoryService {
    
    CategoryDTO findById(Long id);

    Page<CategoryDTO> findAll(Pageable pageable);

    CategoryDTO save(CategoryDTO dto);

    CategoryDTO update(Long id, CategoryDTO dto);

    void delete(Long id);



}
