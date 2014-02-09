package com.xmlsson.samples;

import com.xmlsson.annotations.XmlssonProperty;
import com.xmlsson.annotations.XmlssonSubstructure;

public class AnnotatedWithSubstructure {

    @XmlssonProperty("//foobar/text()")
    public String foobar;

    @XmlssonSubstructure(value = "//substruct", structureClass = AnnotatedSimple.class)
    public AnnotatedSimple substructure;

}
