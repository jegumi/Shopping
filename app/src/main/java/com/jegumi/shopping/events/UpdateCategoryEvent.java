package com.jegumi.shopping.events;

import com.jegumi.shopping.model.Category;

public class UpdateCategoryEvent {

    public Category category;

    public UpdateCategoryEvent(Category category) {
        this.category = category;
    }
}
