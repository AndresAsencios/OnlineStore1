package com.pluralsight;

import java.util.Locale;

public class Product {
    private String sku;
    private String name;
    private double price;
    private String department;

    public Product(String sku, String name, double price, String department) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.department = department;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        // Locale.US: always a dot as decimal separator, also on a Dutch computer
        return String.format(Locale.US, "%-8s %-32s $%8.2f   %s", sku, name, price, department);
    }
}