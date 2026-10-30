package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class OnlineStore {
    public static void main(String[] args) {
        ArrayList<Product> inventory = loadInventory("src/main/resources/products.csv");
        ArrayList<Product> cart = new ArrayList<>();
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
                    displayProducts(inventory, cart, scanner);
                    break;
                case "2":
                    displayCart(cart, scanner);
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
        }
        catch (IOException e) {
            System.out.println("Could not load " + fileName + ": " + e.getMessage());
        }
        return inventory;
    }

    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        System.out.println();
        System.out.println("=== Products ===");
        for (Product product : inventory) {
            System.out.println(product);
        }
        System.out.println("Enter a product SKU (X to go back): ");
        String sku = scanner.nextLine().trim();

        for (Product product : inventory) {
            if (product.getSku().equalsIgnoreCase(sku)) {
                cart.add(product);
                System.out.println(product.getName() + " added to cart!");
                return;
            }
            if (sku.equalsIgnoreCase("X")) {
                return;
            }
        }
        System.out.println("Product not found.");
        // TODO: let the customer search or filter the list
        // TODO: let the customer add a product to the cart (by SKU)
        // TODO: let the customer go back to the home screen
    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        while(true) {

        System.out.println();
        System.out.println("=== Your Cart ===");
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        double total = 0;

        for (Product product : cart) {
            System.out.println(product);
            total += product.getPrice();
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

                cart.clear();
                return;

            case "2":
                System.out.print("Enter the SKU of the product to remove: ");
                String removeSku = scanner.nextLine().trim();
                Product productToRemove = null;
                for (Product product : cart) {
                    if (product.getSku().equalsIgnoreCase(removeSku)) {
                        productToRemove = product;
                        break;
                    }
                }
                if (productToRemove != null) {
                    cart.remove(productToRemove);
                    System.out.println(productToRemove.getName() + " removed from cart!");
                } else {
                    System.out.println("Product not found in cart.");
                }
                break;

            case "3":
                return;

            default:
                System.out.println("Invalid option.");
        }
        // TODO: show every product in the cart and the total amount
        // TODO: let the customer check out, remove a product, or go back
        }
    }
}
