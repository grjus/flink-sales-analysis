package com.grjus.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductOrderItemDto {
    public String productId;
    public String name;
    public Float pricePerUnit;
    public Integer quantity;
    public Float totalPrice;
    public String category;
}
