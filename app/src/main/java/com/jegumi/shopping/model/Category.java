package com.jegumi.shopping.model;

import java.io.Serializable;

// This package map the json objects into Java objects
// The names, etc are not Java friendly (starting with UpperCase) you can map the name with the property
// but in this example I didn't have too much time, so I just let the names as we received to save time

public class Category implements Serializable {

    public String CategoryId;
    public String Name;
    public int ProductCount;
}
