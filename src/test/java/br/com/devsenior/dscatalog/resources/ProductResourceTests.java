package br.com.devsenior.dscatalog.resources;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.devsenior.dscatalog.dto.ProductDTO;
import br.com.devsenior.dscatalog.services.ProductService;
import br.com.devsenior.dscatalog.services.exceptions.DataBaseException;
import br.com.devsenior.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.devsenior.factory.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper mapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        productDTO = Factory.creaProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(service.findAll(ArgumentMatchers.any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //Testando update passando any(), ou seja, qualquer argumento
        //when(service.update(eq(existingId), any())).thenReturn(productDTO);

        when(service.update(existingId, productDTO)).thenReturn(productDTO);
        when(service.update(nonExistingId, productDTO)).thenThrow(ResourceNotFoundException.class);

        when(service.save(productDTO)).thenReturn(productDTO);
        

        // Quando o método é void, primeiro faz a o resultado esperado, depois o when...
        doNothing().when(service).delete(existingId);// Se o ID existe, não faça nada.
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DataBaseException.class).when(service).delete(dependentId);


    }

    @Test
    public void deleteShouldDeleteProductWhenExistId() throws Exception {
        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
            .accept(MediaType.APPLICATION_JSON));
        
        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnResourceNotFoundWhenNonExistId() throws Exception {
        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

        result.andExpect(jsonPath("$.status").exists());
        result.andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void deleteShouldReturnDataBaseExceptionWhenDependentId() throws Exception {
        ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId));
        
        result.andExpect(status().isBadRequest());

        result.andExpect(jsonPath("$.timestamp").exists());
        result.andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void saveShouldReturnCreatedAndProductDto() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
            .content(jsonBody)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON));
        
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {
        
        String jsonBody = mapper.writeValueAsString(productDTO);
        
        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = mapper.writeValueAsString(productDTO);
        
        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

        result.andExpect(jsonPath("$.timestamp").exists());
        result.andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {

        ResultActions result = mockMvc.perform(get("/products")
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        // Analisar o corpo da resposta com jsonPath
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    //Teste tratado na camanda handler para retornar not found 404
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
        
    }

    
}
