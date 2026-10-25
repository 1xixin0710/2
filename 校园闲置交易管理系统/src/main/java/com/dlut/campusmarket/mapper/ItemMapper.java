package com.dlut.campusmarket.mapper;

import com.dlut.campusmarket.model.Item;
import com.dlut.campusmarket.model.ItemQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {

    List<Item> search(ItemQuery query);

    Item findById(Long id);

    int insert(Item item);

    int update(Item item);

    int deleteById(Long id);

    int countAll();

    int countByStatus(String status);

    int changeStatus(@Param("id") Long id, @Param("expected") String expected, @Param("next") String next);
}
