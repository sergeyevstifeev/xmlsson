package com.xmlsson;

import com.xmlsson.samples.AnnotatedPrivate;
import com.xmlsson.samples.AnnotatedSimple;
import com.xmlsson.samples.NoDefaultConstructor;
import com.xmlsson.samples.NotAnnotated;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ObjectMapperTest {

    @Test
    public void testReadValueWorksForSimplestCase() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AnnotatedSimple instance = objectMapper.readValue("<foo>baz</foo>", AnnotatedSimple.class);
        assertEquals("baz", instance.foo);
    }

    @Test
    public void testReadValueWorksForPrivateFields() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AnnotatedPrivate instance = objectMapper.readValue("<foo>baz</foo>", AnnotatedPrivate.class);
        assertEquals("baz", instance.getFoo());
    }

    @Test
    public void testReadValueDoesNotCrashOnNotAnnotated() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue("<foo>baz</foo>", NotAnnotated.class);
    }

    @Test(expected = MappingException.class)
    public void testReadValueCrashesOnNoDefaultConstructor() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue("<foo>baz</foo>", NoDefaultConstructor.class);
    }
}
