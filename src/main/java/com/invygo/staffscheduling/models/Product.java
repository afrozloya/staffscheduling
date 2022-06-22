package com.invygo.staffscheduling.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "products")
public class Product {
    @Id
    String id;
    String prodName;
    String prodDesc;
    String prodPrice;
    String prodImage;

    public Product() {
    }

    public Product(String prodName, String prodDesc, String prodPrice, String prodImage) {
        this.prodName = prodName;
        this.prodDesc = prodDesc;
        this.prodPrice = prodPrice;
        this.prodImage = prodImage;
    }
}
