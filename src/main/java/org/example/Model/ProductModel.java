package org.example.Model;

public record ProductModel(
        int id,
        String sku,
        String name,
        double price,
        int stock
) {}
