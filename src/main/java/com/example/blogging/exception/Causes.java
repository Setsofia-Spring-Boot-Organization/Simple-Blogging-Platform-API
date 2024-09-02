package com.example.blogging.exception;

public enum Causes {
    NO_EMPTY_FIELDS_ALLOWED("No empty fields allowed"),
    THE_FOLLOWING_FIELDS_ARE_EMPTY("The following fields are empty: "),
    NULL_ITEM_RECEIVED("The blog body is null/empty"),
    BLOG_ID_DOES_NOT_EXIST("id not found");

    public final String label;
    Causes(String label) {
        this.label = label;
    }
}
