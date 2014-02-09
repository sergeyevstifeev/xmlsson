package com.xmlsson;

import com.xmlsson.annotations.XmlssonProperty;
import com.xmlsson.annotations.XmlssonSubstructure;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
        Node result = (Node) xPath.evaluate(annotation.value(), document, XPathConstants.NODE);
        field.setAccessible(true);
        field.set(instance, convertNodeToField(result, field.getType()));
    }

    private <T> List<T> nodeListToArray(NodeList nodeList, Class T) {
        int length = nodeList.getLength();
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add((T) convertNodeToField(nodeList.item(i), T));
        }
        return result;
    }

    private Object convertNodeToField(Node result, Class clazz) {
        if (clazz == String.class) {
            return result.getTextContent();
        } else if (clazz == int.class) {
            return Integer.valueOf(result.getNodeValue());
        } else if (clazz == long.class) {
            return Long.valueOf(result.getNodeValue());
        } else if (clazz == float.class) {
            return Float.valueOf(result.getNodeValue());
        } else if (clazz == double.class) {
            return Double.valueOf(result.getNodeValue());
        } else if (clazz == String[].class) {
            NodeList childNodes = result.getChildNodes();
            String[] strings = nodeListToArray(childNodes, String.class).toArray(new String[childNodes.getLength()]);
            return strings;
        } else {
            throw new IllegalArgumentException("Unsupported property type: " + clazz);
        }
    }

}
