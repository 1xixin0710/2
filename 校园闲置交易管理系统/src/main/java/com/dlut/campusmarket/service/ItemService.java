package com.dlut.campusmarket.service;

import com.dlut.campusmarket.mapper.ItemMapper;
import com.dlut.campusmarket.model.Item;
import com.dlut.campusmarket.model.ItemQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    private static final Map<String, String> CATEGORY_IMAGES = Map.of(
            "电子数码", "/images/laptop.jpg",
            "书籍资料", "/images/books.jpg",
            "运动户外", "/images/bike.jpg",
            "宿舍生活", "/images/chair.jpg",
            "其他闲置", "/images/lamp.jpg"
    );

    private final ItemMapper itemMapper;

    public ItemService(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public List<Item> search(ItemQuery query) {
        normalizeQuery(query);
        return itemMapper.search(query);
    }

    public Item findById(Long id) {
        return itemMapper.findById(id);
    }

    @Transactional
    public void create(Item item) {
        normalizeItem(item);
        itemMapper.insert(item);
    }

    @Transactional
    public boolean update(Long id, Item item) {
        if (itemMapper.findById(id) == null) {
            return false;
        }
        item.setId(id);
        normalizeItem(item);
        itemMapper.update(item);
        return true;
    }

    @Transactional
    public boolean delete(Long id) {
        return itemMapper.deleteById(id) > 0;
    }

    public int countAll() {
        return itemMapper.countAll();
    }

    public int countByStatus(String status) {
        return itemMapper.countByStatus(status);
    }

    private void normalizeQuery(ItemQuery query) {
        query.setKeyword(blankToNull(query.getKeyword()));
        query.setCategory(blankToNull(query.getCategory()));
        query.setStatus(blankToNull(query.getStatus()));
    }

    private void normalizeItem(Item item) {
        item.setTitle(item.getTitle().trim());
        item.setDescription(item.getDescription().trim());
        item.setSellerName(item.getSellerName().trim());
        item.setContact(item.getContact().trim());
        item.setDeliveryOptions(String.join(",", item.getDeliveryChoices()));
        if (item.getImageUrl() == null || item.getImageUrl().isBlank()) {
            item.setImageUrl(CATEGORY_IMAGES.getOrDefault(item.getCategory(), "/images/lamp.jpg"));
        }
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
