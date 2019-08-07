package bsz.expo.trafo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Eine TrafoPipeline repräsentiert eine bestimmte Datenverarbeitung.
 * <p>
 * Eine Trafopieline wird mit Hilfe einer Pipeline-Definition konfiguriert und besteht aus einer
 * Reihe {@link TrafoPipe}-Objekten, die in eine Kette geschaltet sind.
 * <p>
 * Eine TrafoPipeline verfügt über eine Liste von {@link TrafoConfig}-Objekten, die z.B. über
 * eine JSF-Formular zur Konfiguration angeboten werden können.
 * <p> 
 * Eine Liste von Strings sammelt Meldungen, die nach einer Verarbeitung ausgegeben werden können.
 * <p> 
 * Eine TrafoPipeline ist insbesondere selbst eine {@link TrafoPipe} und verfügt als solche 
 * über einen {@link TrafoContext} sowie einer Liste von String-Parametern zur Konfiguration.  
 * Wie eine TrafoPipe wird eine TrafoPipeline via {@link TrafoPipeline#init} initialisiert, über 
 * {@link TrafoPipeline#process(TrafoTicket)} ausgeführt und mit {@link TrafoPipeline#tidy} 
 * weggeräumt.
 *  
 * @author christof mainberger (christof.mainberger@bsz-bw.de)
 */
public class TrafoPipeline extends TrafoPipe {	
	
		
	public static TrafoPipeline createTrafoPipeline(final File pipelineFile, final TrafoContext trafoContext) 
		throws TrafoException {
		try {
			final TrafoPipeline trafoPipeline = new TrafoPipeline();
			trafoPipeline.trafoContext = trafoContext;
			trafoPipeline.buildPipeline(new FileInputStream(pipelineFile));
			return trafoPipeline;
		} catch (FileNotFoundException e) {
			throw new TrafoException("Konnte die Pipeline-Definition nicht finden: " + pipelineFile.getPath(), e);
		}
	}
	
	private TrafoPipe trafoReader;
	
	private List<TrafoConfig> trafoConfiguration = new ArrayList<>();
	public List<TrafoConfig> getConfiguration() { return trafoConfiguration; };
	public TrafoConfig getConfiguration(String name) {
		for (TrafoConfig config : trafoConfiguration) {
			if (name.equals(config.getName())) {
				return config;
			}
		}
		return null;
	}
	
	
	private TrafoContext trafoContext;		
	public TrafoContext getTrafoContext() { return trafoContext; }
	public void setTrafoContext(final TrafoContext trafoContext) { this.trafoContext = trafoContext; }

	private Map<String, Object> attributes = new HashMap<>();
	public Object getAttribute(final String key) { return attributes.get(key); }
	public void setAttribute(final String key, Object object) { this.attributes.put(key, object); }		
		
	final private List<String> messages = new ArrayList<>();
	public List<String> getMessages() { return messages; }
	public void addMessage(final String value) { messages.add(value); }
		
	final private List<TrafoResult> results = new ArrayList<>();
	public List<TrafoResult> getResults() { return results; }
	public void addResult(final TrafoResult trafoResult) { results.add(trafoResult); }
	public TrafoResult getResult(String name) {
		for (TrafoResult result : results) {
			if (name.equals(result.getName())) {
				return result;
			}
		}
		return null;
	}
		
	public String getRealPath(final String path) { return trafoContext.getRealPath(path); }	
	public String getTempPath(final String filename) { return trafoContext.getTempPath(filename); }
	public String getDataPath(final String filename) { return trafoContext.getDataPath(filename); }
	public String getPipelinePath(final String filename) { return trafoContext.getPipelinePath(filename); }
	
	private String name;
	public String getName() { return name; }	
	
	private String description;
	public String getDescription() { return description; }		
	
	/**
	 * configPipeline konfiguriert eine TrafoPipeline anhand einer Pipelinedefinition.
	 * <p>
	 * Die Pipelinedefinition ist z.B. eine Pipeline-XML-Datei und wird in einem InputStream übergeben. 
	 * 
	 * @param pipelineDefinition die übergebene Pipelinedefinition
	 */
	public void buildPipeline(final InputStream pipelineDefinition) {		
		try {		
			final PipelineHandler pipelineHandler = new PipelineHandler(this);			
			final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
			xmlReader.setContentHandler(pipelineHandler);
			xmlReader.parse(new InputSource(pipelineDefinition));						
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 	
	}
	
	/**
	 * init initiatlisiert die TrafoPipeline
	 * 
	 * Dabei wird zunächst die erste {@link TrafoPipe} - und damit alle anderen Pipes der Pipeline - initialisiert
	 * und schließlich die Pipeline selbst als TrafoPipe-Objekt.
	 */
	@Override
	public void init() throws TrafoException {
		trafoReader.init();
		super.init();
	}
	
	/**
	 * process führt die Datenverarbeitung der TrafoPipeline aus.
	 * <p>
	 * Dabei werden zunächst die {@link TrafoConfig}-Objekte in das trafoTicket übertragen, dann werden 
	 * die Daten durch Übergabe des TrafoTickets an die erste Pipe der Pipeline verarbeitet. 
	 * (Danach wird das Ticket ggf. an ein der Pipeline nachfolgendes Pipe-Objekt weitergereicht.) 
	 * 
	 * @param trafoTicket enthält die Daten, die verarbeitet werden
	 */
	@Override
	public void process(final TrafoTicket trafoTicket) throws TrafoException {
		for (TrafoConfig config : trafoConfiguration) {
			trafoTicket.put(config.getName(), config.getValue());
		}			
		trafoReader.process(trafoTicket);		
		super.process(trafoTicket);
	}	
	
	/**
	 * finit räumt nach der Verabeitung geöffnete Ressourcen wieder auf und nimmt abschließende Aggregatioen vor.	
	 */
	@Override
	public void finit() throws TrafoException {
		trafoReader.finit();
		super.finit();
	}

	private class PipelineHandler extends DefaultHandler {	
		
		private final TrafoPipeline trafopipeline;	
		private TrafoPipe currPipe = null;	
		private TrafoConfig currConfig = null;
		
		public PipelineHandler(TrafoPipeline trafopipeline) {
			this.trafopipeline = trafopipeline;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("config".equals(localName)) {
				if ("text".equals(attributes.getValue("typ"))) {
					currConfig = new TrafoConfig("text");
					currConfig.setValue(attributes.getValue("default"));
				} else if ("datum".equals(attributes.getValue("typ"))) {
					currConfig = new TrafoConfig("datum");
				} else if ("zahl".equals(attributes.getValue("typ"))) {
					currConfig = new TrafoConfig("zahl");
				} else if ("file".equals(attributes.getValue("typ"))) {
					currConfig = new TrafoConfig("file");
				} else if ("menu".equals(attributes.getValue("typ"))) {
					currConfig = new TrafoConfig("menu");
				} else {
					throw new SAXException("Falscher oder fehlender Konfigurations-Typ: " + attributes.getValue("typ"));
				}
				currConfig.setName(attributes.getValue("name"));
				currConfig.setLabel(attributes.getValue("label"));				
				trafoConfiguration.add(currConfig);
			} else if ("option".equals(localName)) {
//				String value = attributes.getValue("value");
//				String label = attributes.getValue("label");
//				List<TrafoOption> options = currConfig.getOptions();
//				options.add(new TrafoOption(value, label));
				currConfig.addOption(attributes.getValue("value"), attributes.getValue("label"));
			} else if ("pipe".equals(localName)) {
				TrafoPipe pipe;
				try {
					pipe = (TrafoPipe) Class.forName(attributes.getValue("className")).newInstance();
					pipe.setTrafoPipeline(trafopipeline);
					if ("bsz.swbtrafo.TrafoPipeline".equals(attributes.getValue("className"))) {
						((TrafoPipeline)pipe).buildPipeline(new FileInputStream(getRealPath("/WEB-INF/pipelines" + attributes.getValue("config"))));
					} 
				} catch (Exception e) {
					throw new SAXException(e);
				}				
				if (currPipe != null) {
					currPipe.setNext(pipe);
				} else {
					trafoReader = pipe;
				}
				currPipe = pipe;
			} else if ("parameter".equals(localName)) {
				if (currPipe != null) {
					currPipe.addParameter(attributes.getValue("name"), attributes.getValue("value"));
				} else {
					this.trafopipeline.addParameter(attributes.getValue("name"), attributes.getValue("value"));
				}
			} else if ("listener".equals(localName)) {
				try {
					TrafoListener listener = (TrafoListener) Class.forName(attributes.getValue("className")).newInstance();
					currPipe.addListener(listener);
				} catch (Exception e) {
					throw new SAXException(e);
				}
			} else if ("pipeline".equals(localName)) {			
				name = attributes.getValue("name");
				description = attributes.getValue("description");
			} else {
				throw new SAXException("Falsches Element: " + localName);
			}
		}		
	}
}
