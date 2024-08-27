package com.example.blogging.exception;

public enum Causes {
    NO_EMPTY_FIELDS_ALLOWED("No empty fields allowed."),
    THE_FOLLOWING_FIELDS_ARE_EMPTY("The following fields are empty: ");

    public final String label;
    Causes(String label) {
        this.label = label;
    }
}
