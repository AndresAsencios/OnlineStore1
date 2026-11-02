package com.pluralsight;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OnlineStore {
    public static void main(String[] args) {
        ArrayList<Product> inventory = loadInventory("src/main/resources/products.csv");
        HashMap<String, Integer> quantities = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        String choice = "";
        while (!choice.equals("3")) {
            System.out.println();
            System.out.println("=== Welcome to the Online Store ===");
            System.out.println("1) Display Products");
            System.out.println("2) Display Cart");
            System.out.println("3) Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayProducts(inventory, quantities, scanner);
                    break;

                case "2":
                    displayCart(inventory, quantities, scanner);
                    break;
                case "3":
                    System.out.println("Thank you for shopping with us!");
                    break;
                default:
                    System.out.println("Please enter 1, 2 or 3.");
            }
        }
    }

    public static ArrayList<Product> loadInventory(String fileName) {
        ArrayList<Product> inventory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // skip the header line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String sku = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                String department = parts[3];
                inventory.add(new Product(sku, name, price, department));
            }
        } catch (IOException e) {
            System.out.println("Could not load " + fileName + ": " + e.getMessage());
        }
        return inventory;
    }

    public static void displayProducts(ArrayList<Product> inventory, HashMap<String, Integer> quantities, Scanner scanner) {
        System.out.println();
        System.out.println("=== Products ===");
        for (Product product : inventory) {
            System.out.println(product);
        }
        System.out.print("Enter a product SKU (X to go back): ");
        String sku = scanner.nextLine().trim();
        if (sku.equalsIgnoreCase("X")) {
            return;
        }
        for (Product product : inventory) {
            if (product.getSku().equalsIgnoreCase(sku)) {

                String productSku = product.getSku();

                quantities.put(
                        productSku,
                        quantities.getOrDefault(productSku, 0) + 1
                );

                System.out.println(product.getName() + " added to cart!");
                return;
            }

        }
        System.out.println("Product not found.");
        // TODO: let the customer search or filter the list
        // TODO: let the customer add a product to the cart (by SKU)
        // TODO: let the customer go back to the home screen
    }

    public static void displayCart(ArrayList<Product> inventory, HashMap<String, Integer> quantities, Scanner scanner) {
        while (true) {

            System.out.println();
            System.out.println("=== Your Cart ===");
            if (quantities.isEmpty()) {

                System.out.println("Your cart is empty!");
                return;
            }

            double total = 0;

            for (String sku : quantities.keySet()) {
                for (Product product : inventory) {
                    if (product.getSku().equalsIgnoreCase(sku)) {
                        int quantity = quantities.get(sku);
                        double subtotal = product.getPrice() * quantity;

                        System.out.printf("%s | Qty: %d | Subtotal: $%.2f%n",
                                product.getName(), quantity, subtotal);

                        total += subtotal;
                        break;
                    }
                }
            }
            System.out.printf("Total: $%.2f%n", total);
            System.out.println();
            System.out.println("1) Check Out");
            System.out.println("2) Remove Product");
            System.out.println("3) Return to Main Menu");
            System.out.print("Choose an option: ");

            String cartChoice = scanner.nextLine().trim();

            switch (cartChoice) {
                case "1":
                    System.out.println("=== Checkout ===");
                    System.out.printf("Your total is: $%.2f%n", total);
                    System.out.println("Thank you for your purchase!");
                    StringBuilder receipt = new StringBuilder();

                    receipt.append("=== ONLINE STORE RECEIPT ===\n");
                    for (String sku : quantities.keySet()) {
                        for (Product product : inventory) {
                            if (product.getSku().equalsIgnoreCase(sku)) {

                                int quantity = quantities.get(sku);
                                double subtotal = product.getPrice() * quantity;

                                receipt.append(String.format(
                                        "%s | Qty: %d | Subtotal: $%.2f%n",
                                        product.getName(), quantity, subtotal
                                ));

                                break;
                            }
                        }
                    }
                    receipt.append(String.format("Total: $%.2f%n", total));
                    receipt.append("Thank you for your purchase!\n");

                    quantities.clear();
                    return;

                case "2":
                    System.out.print("Enter the SKU of the product to remove: ");
                    String removeSku = scanner.nextLine().trim();
                    String matchingSku = null;

                    for (String sku : quantities.keySet()) {
                        if (sku.equalsIgnoreCase(removeSku)) {
                            matchingSku = sku;
                            break;
                        }
                    }

                    if (matchingSku != null) {
                        int quantity = quantities.get(matchingSku);

                        if (quantity > 1) {
                            quantities.put(matchingSku, quantity - 1);
                        } else {
                            quantities.remove(matchingSku);
                        }

                        System.out.println("One item removed from cart!");
                    } else {
                        System.out.println("Product not found in cart.");
                    }
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    public static String writeReceipt(String receiptText) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String fileName = now.format(formatter) + ".txt";
        File folder = new File("Receipts");
        folder.mkdirs();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("Receipts/" + fileName))) {
            writer.write(receiptText);
        } catch (IOException e) {
            System.out.println("Could not save receipts" + e.getMessage());
            return null;
        }
        return fileName;
    }

}




