package com.xmlsson;

import com.xmlsson.annotations.XmlssonProperty;

public class AnnotatedSimple {

    @XmlssonProperty("/foo/text()")
    public String foo;

}
