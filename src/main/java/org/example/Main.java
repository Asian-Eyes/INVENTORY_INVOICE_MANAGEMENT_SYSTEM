package org.example;

import java.sql.SQLException;
import org.example.util.db;

public class Main {
    public static void main(String[] args) throws Exception {
        db.ping();
    }
}