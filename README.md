# Overview

Convert from XML to Java via annotations with XPath expressions.

# Usage

For example, create a class:

```java
public class AnnotatedSimple {
    @XmlssonProperty("/foo/text()")
    public String foo;
}
```

then call:

```java
ObjectMapper objectMapper = new ObjectMapper();
AnnotatedSimple instance = objectMapper.readValue("<foo>baz</foo>", AnnotatedSimple.class);
```

A newly created object's foo is set to the value "baz" now.
