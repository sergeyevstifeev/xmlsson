package com.xmlsson.samples;

import com.xmlsson.annotations.XmlssonProperty;

public class AnnotatedMultipleElements {

    @XmlssonProperty("//foo")
    public String[] foo;

}
