package com.dlut.campusmarket.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Item {

    private Long id;

    @NotBlank(message = "请输入物品名称")
    @Size(max = 100, message = "物品名称不能超过 100 个字符")
    private String title;

    @NotBlank(message = "请填写物品描述")
    @Size(max = 1000, message = "物品描述不能超过 1000 个字符")
    private String description;

    @NotBlank(message = "请选择物品分类")
    private String category;

    @NotNull(message = "请输入价格")
    @DecimalMin(value = "0.01", message = "价格必须大于 0")
    private BigDecimal price;

    @NotBlank(message = "请选择成色")
    private String itemCondition;

    @NotBlank(message = "请选择交易地点")
    private String tradeLocation;

    private String deliveryOptions;

    @NotEmpty(message = "请至少选择一种交付方式")
    private List<String> deliveryChoices = new ArrayList<>();

    @NotNull(message = "请选择发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime publishTime;

    @NotBlank(message = "请输入发布人")
    @Size(max = 40, message = "发布人不能超过 40 个字符")
    private String sellerName;

    @NotBlank(message = "请输入联系方式")
    @Size(max = 80, message = "联系方式不能超过 80 个字符")
    private String contact;

    @NotBlank(message = "请选择物品状态")
    private String status;

    private String imageUrl;

    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getTradeLocation() {
        return tradeLocation;
    }

    public void setTradeLocation(String tradeLocation) {
        this.tradeLocation = tradeLocation;
    }

    public String getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(String deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public List<String> getDeliveryChoices() {
        return deliveryChoices;
    }

    public void setDeliveryChoices(List<String> deliveryChoices) {
        this.deliveryChoices = deliveryChoices;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
