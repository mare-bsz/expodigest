package bsz.expo.trafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class TrafoPipe {
		
	protected TrafoPipeline trafoPipeline = null;
	public void setTrafoPipeline(TrafoPipeline trafoPipeline) { this.trafoPipeline = trafoPipeline; }
	public TrafoPipeline getTrafoPipeline() { return trafoPipeline; }

	private TrafoPipe next;
	void setNext(final TrafoPipe next) { this.next = next; }
	
	private Map<String, String> parameters = new HashMap<String, String>();
	public Map<String, String> getParameters() { return parameters; }
	public void addParameter(String name, String value) { parameters.put(name, value); }
	public String getParameter(String key) {
		String result = null;
		result = parameters.get(key);
		if (result == null && trafoPipeline != null) {
			result = trafoPipeline.getParameter(key);
			if (result == null) {
				result = trafoPipeline.getConfiguration(key) != null ? trafoPipeline.getConfiguration(key).getValue().toString() : result;
			}
		}
		return result;
	}
	
	protected Set<String> getParameterNames() {
		final Set<String> parameternames = parameters.keySet();
		parameternames.addAll(trafoPipeline.getParameters().keySet());
		return parameternames;
	}
	
	private List<TrafoListener> listeners = new ArrayList<TrafoListener>();
	void addListener(TrafoListener listener) {
		this.listeners.add(listener); 
	}
	
	public void init() throws TrafoException {
		for (TrafoListener listener : listeners) {
			listener.init(this, trafoPipeline);
		}
		if (next != null) {
			this.next.init();
		}
	}
	
	public void process(TrafoTicket ticket)  throws TrafoException {
		for (TrafoListener listener : listeners) {
			listener.process(ticket);
		}
		if (next != null) {
			this.next.process(ticket);
		}
	}
	
		public void finit() throws TrafoException {
		for (TrafoListener listener : listeners) {
			listener.finit();
		}
		if (next != null) {
			this.next.finit();
		}
	}	
}