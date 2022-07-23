package org.example;

import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.RecordNotFoundException;

import java.math.BigDecimal;
import java.util.*;

public class Database {
    private final HashMap<Long, Product> productsStorage;
    private final HashMap<Long, User> usersStorage;
    private final HashMap<Long, LinkedList<Long>> usersPurchases;
    private final HashMap<Long, HashSet<Long>> productsPurchasers;
    private Long productIdGenerator;
    private Long userIdGenerator;

    public Database() {
        this.productsStorage = new HashMap<>();
        this.usersStorage = new HashMap<>();
        this.usersPurchases = new HashMap<>();
        this.productsPurchasers = new HashMap<>();
        this.productIdGenerator = 0L;
        this.userIdGenerator = 0L;
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

        currentUserPurchases.add(productId);
        currentProductPurchasers.add(userId);
    }

    public void printUserPurchases(Long userId) throws RecordNotFoundException {
        LinkedList<Long> currentUserPurchases = usersPurchases.get(userId);
        if(currentUserPurchases != null) {
            for(Long productId : usersPurchases.get(userId)) {
                System.out.println(productsStorage.get(productId));
                System.out.println("----------------------------");
            }
        } else {
            throw new RecordNotFoundException("There isn't user with id : " + userId + " in database");
        }
    }

    public void printProductPurchasers(Long productId) throws RecordNotFoundException {
        HashSet<Long> currentProductPurchasers = productsPurchasers.get(productId);
        if(currentProductPurchasers != null) {
            for(Long userId : currentProductPurchasers) {
                System.out.println(usersStorage.get(userId));
                System.out.println("----------------------------");
            }
        } else {
            throw new RecordNotFoundException("There isn't product with id : " + productId + " in database");
        }
    }

    public void addUser(String firstName, String lastName, BigDecimal moneyAmount) throws IllegalArgumentException {
        checkIsNull(firstName, "First name");
        checkIsNull(lastName, "Last name");
        checkIsNull(moneyAmount, "Money amount");
        checkMoneyAmount(moneyAmount);

        User toAdd = new User(firstName, lastName, moneyAmount);
        usersStorage.put(userIdGenerator, toAdd);
        usersPurchases.put(userIdGenerator, new LinkedList<>());
        userIdGenerator++;
    }

    public void addProduct(String name, BigDecimal price) throws IllegalArgumentException {
        checkIsNull(name, "Name");
        checkIsNull(price, "Price");
        checkProductPrice(price);

        Product toAdd = new Product(name, price);
        productsStorage.put(productIdGenerator, toAdd);
        productsPurchasers.put(productIdGenerator, new HashSet<>());
        productIdGenerator++;
    }

    public boolean deleteUser(Long userId) {
        User toDelete = usersStorage.get(userId);
        if(toDelete == null) {
            return false;
        }

        for(Long productId : usersPurchases.get(userId)) {
            productsPurchasers.get(productId).remove(userId);
        }

        usersPurchases.remove(userId);
        usersStorage.remove(userId);

        return true;
    }

    public boolean deleteProduct(Long productId) {
        Product toDelete = productsStorage.get(productId);
        if(toDelete == null) {
            return false;
        }

        for(Long userId : productsPurchasers.get(productId)) {
            usersPurchases.get(userId).removeIf(id -> id.equals(productId));
        }

        productsPurchasers.remove(productId);
        productsStorage.remove(productId);

        return true;
    }

    private <K, V> void printKeyValueEntry(Map.Entry<K, V> entry) {
        System.out.println("Id : " + entry.getKey() + "\n" + entry.getValue());
        System.out.println("----------------------------");
    }

    private void checkIsNull(Object obj, String varName) throws IllegalArgumentException {
        if(obj == null) {
            throw new IllegalArgumentException(varName + " is null");
        }
    }

    private void checkMoneyAmount(BigDecimal moneyAmount) throws IllegalArgumentException {
        if(moneyAmount.compareTo(BigDecimal.valueOf(0)) == -1) {
            throw new IllegalArgumentException("Money amount must be greater or equal than 0");
        }
    }

    private void checkProductPrice(BigDecimal price) throws IllegalArgumentException {
        if(price.compareTo(BigDecimal.valueOf(0)) < 1) {
            throw new IllegalArgumentException("Price must contain positive value");
        }
    }
}
