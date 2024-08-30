package br.com.devsenior.dscatalog.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.devsenior.dscatalog.dto.ProductDTO;

public interface IProductService {
    
    ProductDTO findById(Long id);

    Page<ProductDTO> findAll(Pageable pageable);

    ProductDTO save(ProductDTO dto);

    ProductDTO update(Long id, ProductDTO dto);

    void delete(Long id);



}
