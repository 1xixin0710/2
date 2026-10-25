package com.dlut.campusmarket.mapper;

import com.dlut.campusmarket.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    int insert(Order order);

    Order findByIdAndBuyer(@Param("id") Long id, @Param("buyerUsername") String buyerUsername);

    List<Order> findByBuyer(String buyerUsername);

    int markPaid(@Param("id") Long id, @Param("buyerUsername") String buyerUsername);

    int markCancelled(@Param("id") Long id, @Param("buyerUsername") String buyerUsername);

    int markCompleted(@Param("id") Long id, @Param("buyerUsername") String buyerUsername);
}
