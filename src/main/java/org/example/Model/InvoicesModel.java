package org.example.Model;

import java.time.LocalDate;

public record InvoicesModel (
        int id,
        String invoice_no,
        LocalDate created_at,
        double total
) {}
