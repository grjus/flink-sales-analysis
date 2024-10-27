package com.grjus.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDto {
    String productsFilePath;
    String ordersFilePath;
    String outputFilePath;
}
