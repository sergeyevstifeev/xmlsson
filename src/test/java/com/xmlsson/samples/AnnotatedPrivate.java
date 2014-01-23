package com.xmlsson.samples;

import com.xmlsson.annotations.XmlssonProperty;

public class AnnotatedPrivate {

    @XmlssonProperty("/foo/text()")
    private String foo;

    public String getFoo() {
        return foo;
    }
}
