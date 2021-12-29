package co.com.invima.sivicos.srvactasparametricasdocs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "servicios")
@Primary
public class ConfigProperties {
	
	private String urlDocumentos;
	private String urlPlantilla;

	public String getUrlDocumentos() {
		return urlDocumentos;
	}

	public void setUrlDocumentos(String urlDocumentos) {
		this.urlDocumentos = urlDocumentos;
	}

	public String getUrlPlantilla() {
		return urlPlantilla;
	}

	public void setUrlPlantilla(String urlPlantilla) {
		this.urlPlantilla = urlPlantilla;
	}
	
	
}
