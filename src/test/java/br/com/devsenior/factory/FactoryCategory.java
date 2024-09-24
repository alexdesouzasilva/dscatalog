package br.com.devsenior.factory;

import br.com.devsenior.dscatalog.entities.Category;

public class FactoryCategory {
    
    public static Category createCategory() {
        return new Category(1L, "Eletronics");
    }
}
