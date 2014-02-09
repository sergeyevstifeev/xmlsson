package com.xmlsson;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static com.google.common.base.Preconditions.checkArgument;

public final class DOMUtils {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    static {
        DOCUMENT_BUILDER_FACTORY.setNamespaceAware(true);
    }

    private DOMUtils() {
    }

    /**
     * Parses the provided xml string into a {@link org.w3c.dom.Document}.
     * Returns null if provided string is null or empty.
     */
    public static Document xmlToDocument(String xml) {
        checkArgument(StringUtils.isNotBlank(xml));
        try {
            DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            return documentBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalArgumentException("Failed to parse xml.", e);
        }
    }

    /**
     * Return the string representation of the xml document.
     * Typically used for testing.
     */
    public static String documentToString(Document doc) throws TransformerException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        StringWriter writer = new StringWriter();
        t.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

}
