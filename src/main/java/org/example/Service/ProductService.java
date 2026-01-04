package org.example.Service;

import org.example.DAO.ProductDAO;
import org.example.Model.ProductModel;

public class ProductService {
    private final ProductDAO ProductDAO = new ProductDAO();

    public int createProduct(ProductModel product) throws Exception{
        return ProductDAO.createProduct(product);
    }
}