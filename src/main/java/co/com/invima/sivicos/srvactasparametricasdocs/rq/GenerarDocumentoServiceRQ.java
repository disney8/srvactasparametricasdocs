package co.com.invima.sivicos.srvactasparametricasdocs.rq;

import java.util.HashMap;
import java.util.Map;

public class GenerarDocumentoServiceRQ {
	
	private String base64;
	private Map<String, String> tags;
	
	public GenerarDocumentoServiceRQ() {
		this.tags = new HashMap<String,String>();      
	}

	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
	}
	public Map<String, String> getTags() {
		return tags;
	}
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
	
	
}
