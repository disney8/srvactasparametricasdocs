package co.com.invima.sivicos.srvactasparametricasdocs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.actasparametricas.DatosPlantillaDTO;
import co.com.invima.sivicos.srvactasparametricasdocs.config.ConfigProperties;
import co.com.invima.sivicos.srvactasparametricasdocs.rq.GenerarDocumentoServiceRQ;
import co.com.invima.sivicos.srvactasparametricasdocs.service.IPlantillaDatosService;
import reactor.core.publisher.Mono;

@Service
public class PlantillaDatosService implements IPlantillaDatosService {

	private final ConfigProperties config;

	private final WebClient webClient;

	@Autowired
	public PlantillaDatosService(WebClient.Builder webClientBuilder, ConfigProperties config) {
		this.config = config;
		this.webClient = webClientBuilder.baseUrl(this.config.getUrlPlantilla())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
	}
	
	@Override
	public ResponseEntity<GenericResponseDTO> guardarDatosPlantilla(DatosPlantillaDTO datosFinales) {
		try {

			GenericResponseDTO genericResponse = webClient.post().uri("/plantilla/datos")
					.body(Mono.just(datosFinales), GenerarDocumentoServiceRQ.class).retrieve().bodyToMono(GenericResponseDTO.class)
					.block();

			if (genericResponse.getStatusCode() == HttpStatus.CREATED.value()) {
				return new ResponseEntity<>(genericResponse, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(GenericResponseDTO.builder().message("Error: " + e.getMessage())
					.objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}
	}

}
