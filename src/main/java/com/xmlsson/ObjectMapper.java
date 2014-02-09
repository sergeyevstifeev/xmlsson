package com.xmlsson;

import com.xmlsson.annotations.XmlssonProperty;
import com.xmlsson.annotations.XmlssonSubstructure;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.lang.reflect.Field;

public class ObjectMapper {

    private final XPath xPath;

    public ObjectMapper() {
        this.xPath = XPathFactory.newInstance().newXPath();
    }

    public <T> T readValue(String xml, Class<T> clazz) {
        return readValue(DOMUtils.xmlToDocument(xml), clazz);
    }

    public <T> T readValue(Node node, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            final Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(XmlssonProperty.class)) {
                    processProperty(node, instance, field, field.getAnnotation(XmlssonProperty.class));
                } else if (field.isAnnotationPresent(XmlssonSubstructure.class)) {
                    processSubstructure(node, instance, field, field.getAnnotation(XmlssonSubstructure.class));
                }
            }
            return instance;
        } catch (InstantiationException e) {
            throw new MappingException("Make sure to have a default constructor", e);
        } catch (IllegalAccessException | XPathExpressionException e) {
            throw new MappingException(e);
        }
    }

    private <T> void processSubstructure(Node node, T instance, Field field, XmlssonSubstructure annotation) throws XPathExpressionException, IllegalAccessException {
        Node structureNode = (Node) xPath.evaluate(annotation.value(), node, XPathConstants.NODE);
        field.setAccessible(true);
        field.set(instance, readValue(structureNode, annotation.structureClass()));
    }

    private <T> void processProperty(Node document, T instance, Field field, XmlssonProperty annotation) throws XPathExpressionException, IllegalAccessException {
        String result = xPath.evaluate(annotation.value(), document);
        field.setAccessible(true);
        field.set(instance, result);
    }

}
