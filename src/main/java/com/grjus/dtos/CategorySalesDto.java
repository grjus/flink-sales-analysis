package com.grjus.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySalesDto {
    public String category;
    public Float totalSales;
    public Integer count;

    @Override
    public String toString() {
        return category + "," + totalSales + "," + count;
    }
}
