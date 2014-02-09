package com.xmlsson;

import com.xmlsson.annotations.XmlssonProperty;
import com.xmlsson.annotations.XmlssonSubstructure;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import static com.google.common.base.Preconditions.checkArgument;

public class ObjectMapper {

    private final XPath xPath;

    public ObjectMapper() {
        this.xPath = XPathFactory.newInstance().newXPath();
    }

    public <T> T readValue(String xml, Class<T> clazz) {
        return readValue(DOMUtils.xmlToDocument(xml), clazz);
    }

    public <T> T readValue(Document document, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            final Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(XmlssonProperty.class)) {
                    processProperty(document, instance, field, field.getAnnotation(XmlssonProperty.class));
                } else if (field.isAnnotationPresent(XmlssonSubstructure.class)) {
                    processSubstructure(document, instance, field, field.getAnnotation(XmlssonSubstructure.class));
                }
            }
            return instance;
        } catch (InstantiationException e) {
            throw new MappingException("Make sure to have a default constructor", e);
        } catch (IllegalAccessException | XPathExpressionException e) {
            throw new MappingException(e);
        }
    }

    private <T> void processSubstructure(Document document, T instance, Field field, XmlssonSubstructure annotation) {
        throw new IllegalStateException("Not implemented yet");
    }

    private <T> void processProperty(Document document, T instance, Field field, XmlssonProperty propertyAnnotation) throws XPathExpressionException, IllegalAccessException {
        if (propertyAnnotation != null) {
            String xPathExpr = propertyAnnotation.value();
            String result = xPath.evaluate(xPathExpr, document);
            field.setAccessible(true);
            field.set(instance, result);
        }
    }


}
