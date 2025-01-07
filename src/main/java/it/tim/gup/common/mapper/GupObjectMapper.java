package it.tim.gup.common.mapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.json.JsonSanitizer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;


@Component
public class GupObjectMapper {

	@Autowired
	private ObjectMapper mapper;


	public String getValueFromJson(String json, String key) throws IOException {
		String sanitizedJson = JsonSanitizer.sanitize(json);
		JsonNode nodeValue = null;
		String value = "";
		if (json !=null && json.length()!=0) {
			JsonNode node = mapper.readTree(sanitizedJson);
			if (node.has(key)) {
				nodeValue = node.get(key);
				if (nodeValue.isContainerNode()) {
					value = nodeValue.toString();
				}else {
					value = nodeValue.asText();
				}
			}
		}
		return  value;
	} 

	public String putValueInJson(String json, String key, String value) throws IOException {
		String sanitizedJson = JsonSanitizer.sanitize(json);
		JsonNode jsonNode = mapper.readTree(sanitizedJson);
		if(jsonNode.isObject()) {
			jsonNode = ((ObjectNode) jsonNode).put(key,value);
		}
		return jsonNode.toString();

	} 
	public String setNodeObjectInJson(String json, String key, String value) throws IOException {
		String sanitizedJson = JsonSanitizer.sanitize(json);
		JsonNode jsonNode = mapper.readTree(sanitizedJson);
		JsonNode jsonValue = mapper.readTree(value);

		if(jsonNode.isObject()) {
			jsonNode = ((ObjectNode) jsonNode).set(key,jsonValue);		
		}
		return jsonNode.toString();

	} 

	public String create_Json_From_Object(Object object_to_json) throws IOException{
		return mapper.writeValueAsString(object_to_json); 
	}

	/**
	 * Convert Java Object to XML String.
	 * 
	 * @param object_to_xml
	 * @return
	 * @throws IOException
	 */
	public String create_xml_from_Object(Object object_to_xml) throws JAXBException {
		StringWriter sw = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(object_to_xml.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);   
		marshaller.marshal(object_to_xml, sw);
		return sw.toString();
	}

	public Object create_Object_from_xml(String xml, Class<?> object_class) throws JAXBException,XMLStreamException {
		JAXBContext jaxbContext = null;
		Object object = null;

		// START FIX FORTIFY
		XMLInputFactory xif = XMLInputFactory.newFactory();
		xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(xml));
		// END FIX FORTIFY


		jaxbContext = JAXBContext.newInstance(object_class);             
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		//	object = jaxbUnmarshaller.unmarshal(new StringReader(xml));
		object = jaxbUnmarshaller.unmarshal(xsr); // FIX FORTIFY

		return object;

	}
	/**
	 * Verify and gets a value in a String format from an XML body .
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws XPathExpressionException 
	 */
	public String getValueFromXML(String xmlMessage, String key) throws XPathExpressionException {
		String value = null;

		if (xmlMessage != null) {
			InputSource inputXML = new InputSource( new StringReader( xmlMessage ));
			XPath xPath = XPathFactory.newInstance().newXPath();
			value = xPath.evaluate("//"+key, inputXML);
		}

		if ("".equalsIgnoreCase(value))
			return null;

		return value;
	}
	
	public String getValueFromXMLSkipNamespace(String xmlMessage, String key) throws XPathExpressionException {
		String value = null;

		if (xmlMessage != null) {
			InputSource inputXML = new InputSource( new StringReader( xmlMessage ));
			XPath xPath = XPathFactory.newInstance().newXPath();
			value = xPath.evaluate("//*[local-name()='"+key+"']", inputXML);
		}

		if ("".equalsIgnoreCase(value))
			return null;

		return value;
	}

	public String getValueFromXMLWithNestedValue(String xmlMessage, String[] keys) throws XPathExpressionException {
		String value = null;

		if (xmlMessage != null && keys != null && keys.length >= 1) {
			InputSource inputXML = new InputSource( new StringReader( xmlMessage ));
			XPath xPath = XPathFactory.newInstance().newXPath();
			String evaluateString = "";
			for (String key:keys) {
				evaluateString += "/*[local-name()='"+key+"']";
			}	
			value = xPath.evaluate(evaluateString, inputXML);
		}

		if ("".equalsIgnoreCase(value))
			return null;

		return value;
	}
	
	public String create_md5_from_String(String string_to_md5) throws NoSuchAlgorithmException {
		String string_hash = "";
		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		byte[] string_byte = md.digest(string_to_md5.getBytes());
		string_hash = String.format("%1$032X", new BigInteger(1, string_byte));
		return string_hash;
	}

	public ObjectMapper getMapper() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public JsonNode readTree(String json) throws IOException {
		String sanitizedJson = JsonSanitizer.sanitize(json);
		// TODO Auto-generated method stub
		return mapper.readTree(sanitizedJson);
	}


}
