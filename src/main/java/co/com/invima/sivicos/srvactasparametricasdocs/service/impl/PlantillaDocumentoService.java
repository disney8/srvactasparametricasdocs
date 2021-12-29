package co.com.invima.sivicos.srvactasparametricasdocs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import co.com.invima.sivicos.srvactasparametricasdocs.config.ConfigProperties;
import co.com.invima.sivicos.srvactasparametricasdocs.service.IPlantillaDocumento;

@Service
public class PlantillaDocumentoService implements IPlantillaDocumento {

	private final ConfigProperties config;

	private final WebClient webClient;

	@Autowired
	public PlantillaDocumentoService(WebClient.Builder webClientBuilder, ConfigProperties config) {
		this.config = config;
		this.webClient = webClientBuilder.baseUrl(this.config.getUrlPlantilla())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@Override
	public ResponseEntity<GenericResponseDTO> obtenerDocumento(final Integer idPlantilla) {
		try {
            System.out.println(" Ingreso obtenerDocumento " + idPlantilla);
			GenericResponseDTO genericResponse = webClient
					.get().uri(uribuilder -> uribuilder.path("/plantilla/"+ idPlantilla +"/documento").build())
					.retrieve().bodyToMono(GenericResponseDTO.class).block();
			System.out.println("obtenerDocumento getStatusCode: " + genericResponse.getStatusCode());
			if (genericResponse.getStatusCode() == HttpStatus.OK.value()) {
				return new ResponseEntity<>(genericResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(GenericResponseDTO.builder().message("Error: " + e.getMessage())
					.objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}
	}

}
