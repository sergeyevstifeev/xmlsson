package com.xmlsson.samples;

public class NoDefaultConstructor {

    private Object foo;

    public NoDefaultConstructor(Object foo) {
        this.foo = foo;
    }

    public Object getFoo() {
        return foo;
    }
}
