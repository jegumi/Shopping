package com.jegumi.shopping.model;

import java.io.Serializable;

public class Products implements Serializable {

    public String[] AlsoSearched;
    public String Description;
    public Facet[] Facets;
    public int ItemCount;
    public Product[] Listings;
    public String RedirectUrl;
    public String SortType;
}
