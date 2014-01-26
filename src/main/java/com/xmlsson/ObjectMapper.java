package com.xmlsson;

import com.xmlsson.annotations.XmlssonProperty;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.lang.reflect.Field;

public class ObjectMapper {

    private final XPath xPath;

    public ObjectMapper() {
        this.xPath = XPathFactory.newInstance().newXPath();
    }

    public <T> T readValue(String xml, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            final Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                XmlssonProperty propertyAnnotation = field.getAnnotation(XmlssonProperty.class);
                if (propertyAnnotation != null) {
                    String xPathExpr = propertyAnnotation.value();
                    String result = xPath.evaluate(xPathExpr, new InputSource(new StringReader(xml)));
                    field.setAccessible(true);
                    field.set(instance, result);
                }
            }
            return instance;
        } catch (InstantiationException e) {
            throw new MappingException("Make sure to have a default constructor", e);
        } catch (IllegalAccessException | XPathExpressionException e) {
            throw new MappingException(e);
        }
    }

}
