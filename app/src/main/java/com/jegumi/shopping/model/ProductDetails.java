package com.jegumi.shopping.model;

import java.io.Serializable;

public class ProductDetails implements Serializable {

    public String BasePrice;
    public String Brand;
    public String CurrentPrice;
    public boolean InStock;
    public boolean IsInSet;
    public String PreviousPrice;
    public String PriceType;
    public int ProductId;
    public String[] ProductImageUrls;
    public String RRP;
    public String Size;
    public String Sku;
    public String Title;
    public String AdditionalInfo;
    public ProductDetails[] AssociatedProducts;
    public String CareInfo;
    public String Description;
    public ProductDetails[] Variants;
}
