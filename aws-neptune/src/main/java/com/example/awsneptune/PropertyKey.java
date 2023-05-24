package com.example.awsneptune;

import lombok.Getter;

@Getter
public enum PropertyKey {
    VALUE("value");

    private final String value;

    PropertyKey(String value) {
        this.value = value;
    }
}
