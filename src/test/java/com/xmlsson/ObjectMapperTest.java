package com.xmlsson;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ObjectMapperTest {

    @Test
    public void testReadValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AnnotatedSimple instance = objectMapper.readValue("<foo>baz</foo>", AnnotatedSimple.class);
        assertEquals("baz", instance.foo);
    }
}
