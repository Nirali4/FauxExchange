package com.g4.fauxexchange.service.impl;

import java.util.List;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.apache.commons.io.IOUtils;
import org.json.*;

import com.g4.fauxexchange.model.Order;
import com.g4.fauxexchange.model.User;
import com.g4.fauxexchange.model.Currency;
import com.g4.fauxexchange.service.OrderService;
import com.g4.fauxexchange.service.TransactionService;
import com.g4.fauxexchange.dao.OrderRepository;
import com.g4.fauxexchange.dao.CurrencyRepository;
import com.g4.fauxexchange.dao.UserRepository;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService tService;

/* 
Function: Create Order
Description: Pass in a order object to be passed into the collection.
This is based on the users cash value.
If user cash value greater than the order value we can create.
If not we should then fail as this should not happen.

TO DO(Arnold): This could be improved from instead of returning booleans to returning an int with 
the usage of an enum.
 */
    @Override
    public boolean createOrder(Order order) {
        System.out.println("Creating: " + order);

        User user = userRepository.findByUserId(order.getUserId());
        //NUll check
        if(user != null) {
            //User Cash Check >= Order Value
            Currency currency = currencyRepository.findByCode(order.getCode());
            double oc = order.getValue();
            if(order.getType().equals("buy")) {
                double uc = user.getWallet("AUD").getValue();
                if(uc >= oc) {
                    user.updateWallet("AUD", 1, uc - oc);
                    userRepository.save(user);
                    orderRepository.save(order);
                    return true;
                }
            } else {
                if(user.getWallet(order.getCode()) != null) {
                    double uc = user.getWallet(order.getCode()).getValue();
                    double uqty = user.getWallet(order.getCode()).getQty();
                    if(uqty >= order.getQty()) {
                        user.updateWallet(order.getCode(), currency.getRecentPrice(), uqty - order.getQty());
                        userRepository.save(user);
                        orderRepository.save(order);
                        return true;
                    }
                } else {
                    return false;
                }
            }

            return false;
        }
        return false;
    }

/* 
Out of scope
Function: Update Order
Description: This should be able to update the specific order in the collection to with the updated
values
 */
    // @Override
    // public void updateOrder() {

    // }

/* 
Function: Delete Order
Description: Pass in a order object to be deleted from the collection.
In steps:
1. Return money or crypto back to user back to user.
2. Delete the order from the collection.

This technically shouldn't fail.
TO DO(Arnold): Remove hardcoded values
 */
    @Override
    public boolean deleteOrder(Order order) {
        System.out.println("Deleting: " + order);
        User user = userRepository.findByUserId(order.getUserId());
        if(user != null) {
            Currency currency = currencyRepository.findByCode(order.getCode());
            double oc = order.getValue();
            if(order.getType().equals("buy")) {
                double uc = user.getWallet("AUD").getValue();
                user.updateWallet("AUD", 1, uc + oc);
                orderRepository.delete(order);
                return true;
            } else {
                double uqty = user.getWallet(order.getCode()).getQty();
                user.updateWallet(order.getCode(), currency.getRecentPrice(), uqty + order.getQty());
                orderRepository.delete(order);
                return true;
            }
        }
        
        return false;
    }

/* Process Orders
This function runs through all orders and compares to the current market price. And then either
Sells or Buys.
If neither happens orders will exist unless they get deleted. */
    @Override
    @Scheduled(fixedRate = 15000, initialDelay = 15000)
    public void processOrders() {
        System.out.println("- Processing Orders -");
        for(Currency currency : currencyRepository.findAll()) {
            for(Order order : orderRepository.findByCode(currency.code)) {
                if(order.type.equals("buy")) {
                    if(order.price * order.qty >= currency.getRecentPrice() * order.qty) {
                        System.out.println("Buying: " + order);
                        User user = userRepository.findByUserId(order.getUserId());
                        if(user.getWallet(order.code) != null) {
                            user.updateWallet(order.code, currency.getRecentPrice(), user.getWallet(order.code).getQty() + order.qty);
                        } else {
                            user.addWallet(order.code, currency.getRecentPrice(), order.qty);
                        }
                        userRepository.save(user);
                        tService.createTransaction(order);
                        orderRepository.delete(order);
                    }
                } else {
                    if(order.price * order.qty <= currency.getRecentPrice() * order.qty) {
                        System.out.println("Selling: " + order);
                        User user = userRepository.findByUserId(order.getUserId());
                        if(user.getWallet("AUD") != null) {
                            user.updateWallet("AUD", 1, user.getWallet("AUD").getValue() + order.getValue());
                        }
                        userRepository.save(user);
                        tService.createTransaction(order);
                        orderRepository.delete(order);
                    }
                }
            }
        }
    }

/* Returns all the orders */
    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

/* Returns all orders based on code */
    @Override
    public List<Order> getOrders(String code) {
        return orderRepository.findAll();
    }

/* Returns all orders from user */
    @Override
    public List<Order> getOrdersByUserId(String id) {
        return orderRepository.findByUserId(id);
    }

}
