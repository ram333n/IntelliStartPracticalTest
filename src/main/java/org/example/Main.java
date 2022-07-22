package org.example;


import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.RecordNotFoundException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static void printMenu() {
        System.out.println("[0] Display list of all users");
        System.out.println("[1] Display list of all products");
        System.out.println("[2] Buy product");
        System.out.println("[3] Display list of user products by user id");
        System.out.println("[4] Display list of users that bought product by product id");
        System.out.println("[5] Add new user");
        System.out.println("[6] Add new product");
        System.out.println("[7] Delete user by user id");
        System.out.println("[8] Delete product by product id");
        System.out.println("[9] Exit");
    }

    private static void notifyAboutProblem(String message) {
        System.out.println("Error : " + message + "\n");
        printMenu();
    }

    public static void main(String[] args)  {
        Database db = new Database();
        System.out.println("Welcome to IntelliStartPracticalTest application\n");
        printMenu();
        byte cmd = 0;
        do {
            try {
                System.out.print("Enter command : ");
                Scanner scanner = new Scanner(System.in);
                cmd = scanner.nextByte();

                switch (cmd) {
                    case 0 -> {
                        db.printAllUsers();
                    }

                    case 1 -> {
                        db.printAllProducts();
                    }

                    case 2 -> {
                        System.out.print("Enter user id : ");
                        Long userId = scanner.nextLong();
                        System.out.print("Enter product id : ");
                        Long productId = scanner.nextLong();
                        db.buyProduct(userId, productId);
                        System.out.println("Successfully purchased");
                    }

                    case 3 -> {
                        System.out.print("Enter user id : ");
                        Long userId = scanner.nextLong();
                        db.printUserPurchases(userId);
                    }

                    case 4 -> {
                        System.out.print("Enter product id : ");
                        Long productId = scanner.nextLong();
                        db.printProductPurchasers(productId);
                    }

                    case 5 -> {
                        //TODO : add ability to add user
                    }

                    case 6 -> {
                        //TODO : add ability to add product
                    }

                    case 7 -> {
                        //TODO : add ability to delete user
                    }

                    case 8 -> {
                        //TODO : add ability to delete product
                    }

                    case 9 -> {
                        scanner.close();
                        System.out.println("Goodbye! Have a nice day!");
                    }

                    default -> {
                        notifyAboutProblem("Incorrect input");
                    }


                }
            } catch (InputMismatchException exception) {
                notifyAboutProblem("Incorrect input");
            } catch (RecordNotFoundException | NotEnoughMoneyException exception) {
                notifyAboutProblem(exception.getMessage());
            }

        } while (cmd != 9);
    }
}