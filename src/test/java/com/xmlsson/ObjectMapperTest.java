package com.xmlsson;

import com.xmlsson.samples.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ObjectMapperTest {

    @Test
    public void testReadValueWorksForSimplestCase() throws Exception {
        AnnotatedSimple instance = new ObjectMapper().readValue("<foo>baz</foo>", AnnotatedSimple.class);
        assertEquals("baz", instance.foo);
    }

    @Test
    public void testReadValueWorksForPrivateFields() throws Exception {
        AnnotatedPrivate instance = new ObjectMapper().readValue("<foo>baz</foo>", AnnotatedPrivate.class);
        assertEquals("baz", instance.getFoo());
    }

    @Test
    public void testReadValueDoesNotCrashOnNotAnnotated() throws Exception {
        new ObjectMapper().readValue("<foo>baz</foo>", NotAnnotated.class);
    }

    @Test(expected = MappingException.class)
    public void testReadValueCrashesOnNoDefaultConstructor() throws Exception {
        new ObjectMapper().readValue("<foo>baz</foo>", NoDefaultConstructor.class);
    }

    @Test
    public void testSubstructuresWorks() throws Exception {
        AnnotatedWithSubstructure annotatedWithSubstructure = new ObjectMapper().readValue(
                "<bar><substruct><foo>1234</foo></substruct><foobar>foobarval</foobar></bar>",
                AnnotatedWithSubstructure.class);
        assertThat(annotatedWithSubstructure.foobar, equalTo("foobarval"));
        assertThat(annotatedWithSubstructure.substructure.foo, equalTo("1234"));
    }
}
