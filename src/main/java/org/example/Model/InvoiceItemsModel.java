package org.example.Model;

public record InvoiceItemsModel (
        int id,
        int invoice_id,
        int product_id,
        int quantity,
        double unit_price,
        double line_total
) { }
