package com.g4.fauxexchange.service;

import java.util.List;
import com.g4.fauxexchange.model.Order;

public interface OrderService {
    public abstract boolean createOrder(Order order);
    // public abstract void updateOrder();
    public abstract boolean deleteOrder(Order order);
    public abstract void processOrders();
    public abstract List<Order> getOrders();
    public abstract List<Order> getOrders(String code);
    public abstract List<Order> getOrdersByUserId(String id);
}
