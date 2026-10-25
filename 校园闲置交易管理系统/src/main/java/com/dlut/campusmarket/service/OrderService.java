package com.dlut.campusmarket.service;

import com.dlut.campusmarket.mapper.ItemMapper;
import com.dlut.campusmarket.mapper.OrderMapper;
import com.dlut.campusmarket.model.Item;
import com.dlut.campusmarket.model.Order;
import com.dlut.campusmarket.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;

    public OrderService(OrderMapper orderMapper, ItemMapper itemMapper) {
        this.orderMapper = orderMapper;
        this.itemMapper = itemMapper;
    }

    public List<Order> findByBuyer(String buyerUsername) {
        return orderMapper.findByBuyer(buyerUsername);
    }

    public Order findByIdAndBuyer(Long id, String buyerUsername) {
        return orderMapper.findByIdAndBuyer(id, buyerUsername);
    }

    @Transactional
    public Order placeOrder(Item item, User buyer, Order form) {
        if (item == null || !"在售".equals(item.getStatus())) {
            throw new IllegalStateException("该物品当前不可购买");
        }
        if (itemMapper.changeStatus(item.getId(), "在售", "已预订") == 0) {
            throw new IllegalStateException("该物品刚刚被其他同学下单，请刷新页面查看");
        }

        Order order = new Order();
        order.setOrderNo("CM" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        order.setItemId(item.getId());
        order.setItemTitle(item.getTitle());
        order.setItemImageUrl(item.getImageUrl());
        order.setAmount(item.getPrice());
        order.setBuyerUsername(buyer.getUsername());
        order.setBuyerName(buyer.getDisplayName());
        order.setSellerName(item.getSellerName());
        order.setSellerContact(item.getContact());
        order.setDeliveryMethod(form.getDeliveryMethod().trim());
        order.setTradeLocation(form.getTradeLocation().trim());
        order.setPaymentMethod(form.getPaymentMethod().trim());
        order.setContact(form.getContact().trim());
        order.setNote(form.getNote() == null || form.getNote().isBlank() ? null : form.getNote().trim());
        order.setStatus("待付款");
        orderMapper.insert(order);
        return order;
    }

    @Transactional
    public void markPaid(Long id, String buyerUsername) {
        if (orderMapper.markPaid(id, buyerUsername) == 0) {
            throw new IllegalStateException("订单状态已变化，无法重复确认付款");
        }
    }

    @Transactional
    public void cancel(Long id, String buyerUsername) {
        Order order = requireOwned(id, buyerUsername);
        if (orderMapper.markCancelled(id, buyerUsername) == 0) {
            throw new IllegalStateException("只有待付款订单可以取消");
        }
        itemMapper.changeStatus(order.getItemId(), "已预订", "在售");
    }

    @Transactional
    public void complete(Long id, String buyerUsername) {
        Order order = requireOwned(id, buyerUsername);
        if (orderMapper.markCompleted(id, buyerUsername) == 0) {
            throw new IllegalStateException("确认付款后才能确认收货");
        }
        itemMapper.changeStatus(order.getItemId(), "已预订", "已完成");
    }

    private Order requireOwned(Long id, String buyerUsername) {
        Order order = orderMapper.findByIdAndBuyer(id, buyerUsername);
        if (order == null) {
            throw new IllegalStateException("订单不存在");
        }
        return order;
    }
}
