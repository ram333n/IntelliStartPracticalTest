package org.example;

import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.RecordNotFoundException;

import java.math.BigDecimal;
import java.util.*;

public class Database {
    private HashMap<Long, Product> productsStorage;
    private HashMap<Long, User> usersStorage;
    private HashMap<Long, LinkedList<Long>> usersPurchases;
    private HashMap<Long, HashSet<Long>> productsPurchasers;

    public Database() {
        this.productsStorage = new HashMap<>();
        this.usersStorage = new HashMap<>();
        this.usersPurchases = new HashMap<>();
        this.productsPurchasers = new HashMap<>();
    }

    public void printAllUsers() {
        for(Map.Entry<Long, User> entry : usersStorage.entrySet()) {
            printKeyValueEntry(entry);
        }
    }

    public void printAllProducts() {
        for(Map.Entry<Long, Product> entry : productsStorage.entrySet()) {
            printKeyValueEntry(entry);
        }
    }

    public void buyProduct(Long userId, Long productId) throws RecordNotFoundException, NotEnoughMoneyException {
        User user = usersStorage.get(userId);
        if(user == null) {
            throw new RecordNotFoundException("There isn't user with id : " + userId + " in database");
        }

        Product product = productsStorage.get(productId);
        if(product == null) {
            throw new RecordNotFoundException("There isn't product with id : " + productId + " in database");
        }

        BigDecimal userMoneyAmount = user.getMoneyAmount();
        BigDecimal productPrice = product.getPrice();

        if(userMoneyAmount.compareTo(productPrice) < 0) {
            throw new NotEnoughMoneyException (
                "User with id : " + userId + " doesn't have enough money to buy product with id : " + productId + "\n" +
                "User's money amount : " + userMoneyAmount + ", " + "product price : " + productPrice
            );
        }

        user.setMoneyAmount(userMoneyAmount.subtract(productPrice));
        LinkedList<Long> currentUserPurchases = usersPurchases.get(userId);
        HashSet<Long> currentProductPurchasers = productsPurchasers.get(productId);

        if(currentUserPurchases == null) {
            currentUserPurchases = usersPurchases.put(userId, new LinkedList<>());
        }
        currentUserPurchases.add(productId);

        if(currentProductPurchasers == null) {
            currentProductPurchasers = productsPurchasers.put(productId, new HashSet<>());
        }
        currentProductPurchasers.add(userId);
    }

    public void printUserPurchases(Long userId) {
        for(Long productId : usersPurchases.get(userId)) {
            System.out.println(productsStorage.get(productId));
            System.out.println("----------------------------");
        }

    }

    public void printProductPurchasers(Long productId) {
        for(Long userId : productsPurchasers.get(productId)) {
            System.out.println(usersStorage.get(userId));
            System.out.println("----------------------------");
        }
    }

    private <K, V> void printKeyValueEntry(Map.Entry<K, V> entry) {
        System.out.println("Id : " + entry.getKey() + "\n" + entry.getValue());
        System.out.println("----------------------------");
    }
}
