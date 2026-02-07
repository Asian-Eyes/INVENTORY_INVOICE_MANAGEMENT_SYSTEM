package org.example.Service;

import java.util.List;

import org.example.Model.ProductModel;
import org.example.Repository.ProductRepo;

public class ProductService {
    private final ProductRepo productRepo = new ProductRepo();

    public int createProduct(ProductModel product) throws Exception {
        return productRepo.create(product);
    }

    public List<ProductModel> getAllProducts() throws Exception {
        return productRepo.getAll();
    }

    public ProductModel getProductById(int id) throws Exception {
        return productRepo.getAll().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean deductStock(int productId, int quantity) throws Exception {
        ProductModel product = getProductById(productId);
        if (product != null && product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            return productRepo.update(product);
        }
        return false;
    }

    public boolean updateProduct(ProductModel product) throws Exception {
        return productRepo.update(product);
    }

    public boolean deleteProduct(int productId) throws Exception {
        return productRepo.delete(productId);
    }
}