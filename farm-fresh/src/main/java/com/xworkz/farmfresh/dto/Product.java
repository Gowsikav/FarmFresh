package com.xworkz.farmfresh.dto;

import lombok.Data;

@Data
public class Product {
    private int id;
    private String name;
    private String image;
    private String description;
    private double price;
}

