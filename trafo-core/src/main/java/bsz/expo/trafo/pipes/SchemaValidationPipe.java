package bsz.expo.trafo.pipes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Serializer;
import nu.xom.converters.DOMConverter;

public class SchemaValidationPipe extends TrafoPipe {	
	
	Validator validator;
	DOMConverter domConverter;
    Serializer serializer;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	@Override
	public void init() throws TrafoException {
		try {
			SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			Schema schema = sf.newSchema( new URL(getParameter("schemaUrl")) );
			validator = schema.newValidator();
            serializer = new Serializer(baos, "UTF-8");
		} catch (Exception e) {
			throw new TrafoException(e);
		}  
		super.init();
	}
	
	@Override
	public void process(TrafoTicket ticket) throws TrafoException {
		try {
			serializer.write(ticket.getDocument());
			serializer.flush();
			validator.validate(new StreamSource(new ByteArrayInputStream(baos.toByteArray())));
			baos.reset();
			super.process(ticket);
		} catch (SAXException e) {			
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
