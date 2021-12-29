package co.com.invima.sivicos.srvactasparametricasdocs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.actasparametricas.BloquePlantillaDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.actasparametricas.DatosPlantillaDTO;
import co.com.invima.sivicos.srvactasparametricasdocs.config.ConfigProperties;
import co.com.invima.sivicos.srvactasparametricasdocs.enums.ExtensionesArchivosEnum;
import co.com.invima.sivicos.srvactasparametricasdocs.rq.GenerarDocumentoServiceRQ;
import co.com.invima.sivicos.srvactasparametricasdocs.service.IDocumentoService;
import co.com.invima.sivicos.srvactasparametricasdocs.service.ProcessService;
import co.com.invima.sivicos.srvactasparametricasdocs.service.dto.ArchivoResponseDTO;
import co.com.invima.sivicos.srvactasparametricasdocs.service.helpers.DocumentoHelper;
import co.com.invima.sivicos.srvactasparametricasdocs.utils.Utils;
import reactor.core.publisher.Mono;

@Service
public class DocumentoService implements IDocumentoService {
	
	private static final String MENSAJE_EXTENSION_NO_ESPERADA = "Por favor verificar documento extensión no esperada";
	private static final String BASE64 = "base64";
	private static final String TAGS = "tags";

	private final ConfigProperties config;

	private final WebClient webClient;

	private final PlantillaDocumentoService plantillaDocumentoService;

	private final PlantillaDatosService plantillaDatosService;

	private final ProcessService service;

	private DatosPlantillaDTO datosPlantilla;

	private GenericResponseDTO genericResponse;

	private GenerarDocumentoServiceRQ generarDocumento;

	private Utils utiles;

	@Autowired
	public DocumentoService(WebClient.Builder webClientBuilder, ConfigProperties config,
			PlantillaDocumentoService plantillaDocumentoService, PlantillaDatosService plantillaDatosService,
			ProcessService service) {
		this.plantillaDocumentoService = plantillaDocumentoService;
		this.plantillaDatosService = plantillaDatosService;
		this.config = config;
		this.service = service;
		this.datosPlantilla = new DatosPlantillaDTO();
		this.genericResponse = new GenericResponseDTO();
		this.generarDocumento = new GenerarDocumentoServiceRQ();
		this.utiles = new Utils();
		this.webClient = webClientBuilder.baseUrl(this.config.getUrlDocumentos())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
	}

	
	public ResponseEntity<GenericResponseDTO> obtenerVariablesDocumento(final ArchivoResponseDTO documento) {
		try {
			GenerarDocumentoServiceRQ generarDocumento = new GenerarDocumentoServiceRQ();
			generarDocumento.setBase64(documento.getContenido());
			generarDocumento.setTags(null);
			String extension = utiles.getFileExtension(documento.getNombre());
			if (extension.equals(ExtensionesArchivosEnum.DOCX.getExtension())) {
				return obtenerEtiquetas();
			} else if (extension.equals(ExtensionesArchivosEnum.XLS.getExtension())
					|| extension.equals(ExtensionesArchivosEnum.XLSX.getExtension())) {
				this.genericResponse =  this.service.getListaEtiquetas( documento.getContenido());
				this.genericResponse.setObjectResponse(
						DocumentoHelper.transformarTagsToAtributos(genericResponse.getObjectResponse(), Boolean.FALSE));
				return new ResponseEntity<>(genericResponse, HttpStatus.OK);
			}else {
                this.genericResponse.setMessage(MENSAJE_EXTENSION_NO_ESPERADA);
                this.genericResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(GenericResponseDTO.builder().message("Error: " + e.getMessage())
					.objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<GenericResponseDTO> actualizarDatosDocumento(final List<BloquePlantillaDTO> lstBloques,
			final Boolean indVistaPrevia) {
		ResponseEntity<GenericResponseDTO> respuesta = null;
		try {
			if (lstBloques != null & lstBloques.size() > 0) {
				respuesta = remplazarEtiquetasDocumento(lstBloques);
				ObjectMapper mapper = new ObjectMapper();
				String jsonStr = mapper.writeValueAsString(respuesta.getBody());
				this.genericResponse = respuesta.getBody();
				if (respuesta.getBody().getStatusCode() == HttpStatus.OK.value()) {
					if (!indVistaPrevia) {
						this.datosPlantilla = (DatosPlantillaDTO) respuesta.getBody().getObjectResponse();
						List<Object> lstObjetos = new ArrayList<Object>();
						lstObjetos.addAll(lstBloques);
						this.datosPlantilla.setDatos(lstObjetos);
						this.datosPlantilla.setUsuarioCrea(lstBloques.get(0).getUsuarioCrea());
						this.datosPlantilla.setIdPlantilla(lstBloques.get(0).getIdPlantilla());

						String jsonStr1 = mapper.writeValueAsString(this.datosPlantilla);
						System.out.print("Inicio datosPlantilla " + jsonStr1);
						respuesta = this.plantillaDatosService.guardarDatosPlantilla(this.datosPlantilla);
						System.out.println("getStatusCode " + respuesta.getBody().getStatusCode());
						String jsonStr2 = mapper.writeValueAsString(respuesta);
						System.out.println("Fin datosPlantilla " + jsonStr2);
						genericResponse = respuesta.getBody();
						if (respuesta.getBody().getStatusCode() == HttpStatus.CREATED.value()) {
							return new ResponseEntity<>(this.genericResponse, HttpStatus.OK);
						} else {
							return new ResponseEntity<>(this.genericResponse, HttpStatus.BAD_REQUEST);
						}
					}else {
						return new ResponseEntity<>(this.genericResponse, HttpStatus.OK);
					}
				} else {
					return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
				}

			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(GenericResponseDTO.builder().message("Error: " + e.getMessage())
					.objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<GenericResponseDTO> remplazarEtiquetasDocumento(final List<BloquePlantillaDTO> lstBloques) {
		try {
			this.generarDocumento = DocumentoHelper.asignarVariables(lstBloques);
			System.out.println("Despues asignar variables ");
			ResponseEntity<GenericResponseDTO> responseDocumento = plantillaDocumentoService
					.obtenerDocumento(Math.toIntExact(lstBloques.get(0).getIdPlantilla()));
			ObjectMapper mapper = new ObjectMapper();

			String jsonStr = mapper.writeValueAsString(responseDocumento.getBody().getObjectResponse());
			System.out.println("Inicio obtener archivo contenido " + jsonStr);

			ArchivoResponseDTO archivo = mapper.readValue(jsonStr, ArchivoResponseDTO.class);
			this.generarDocumento.setBase64(archivo.getContenido());

			if (responseDocumento.getBody().getStatusCode() == HttpStatus.OK.value()) {
				String extension = utiles.getFileExtension(archivo.getNombreArchivo());
				if (extension.equals(ExtensionesArchivosEnum.DOCX.getExtension())) {
					return procesarDocumentoDocx();
				} else if (extension.equals(ExtensionesArchivosEnum.XLS.getExtension())
						|| extension.equals(ExtensionesArchivosEnum.XLSX.getExtension())) {
					Map<String, Object> datos = new HashMap<>();
					datos.put(BASE64, archivo.getContenido());
					datos.put(TAGS, this.generarDocumento.getTags());
					this.genericResponse = this.service.getPDF(datos);
                   
					if (genericResponse.getStatusCode() == HttpStatus.OK.value()) {
						this.datosPlantilla.setContenido((String) genericResponse.getObjectResponse());
						genericResponse.setObjectResponse(datosPlantilla);
						return new ResponseEntity<>(genericResponse, HttpStatus.OK);
					} else {
						return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
					}
				}else {
                    this.genericResponse.setMessage(MENSAJE_EXTENSION_NO_ESPERADA);
                    this.genericResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
					return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(GenericResponseDTO.builder().message("Error: " + e.getMessage())
					.objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<GenericResponseDTO> procesarDocumentoDocx() {
		try {
			this.genericResponse = webClient.post().uri("/generarDocumento/remplazarEtiquetasDeBase64")
					.body(Mono.just(this.generarDocumento), GenerarDocumentoServiceRQ.class).retrieve()
					.bodyToMono(GenericResponseDTO.class).block();
             ObjectMapper mapper = new ObjectMapper();
				String jsonStr = mapper.writeValueAsString(this.genericResponse);
				System.out.println("respuesta word " + jsonStr);
			if (this.genericResponse.getStatusCode() == HttpStatus.OK.value()) {
				//String docRespues = Utils.converWordPdf(Base64.decodeBase64((String) this.genericResponse.getObjectResponse()));

				this.datosPlantilla.setContenido((String) this.genericResponse.getObjectResponse());
				String jsonStr1 = mapper.writeValueAsString(this.datosPlantilla);
				System.out.println("datosPlantilla " + jsonStr1);
				this.genericResponse.setObjectResponse(datosPlantilla);
				return new ResponseEntity<>(this.genericResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(this.genericResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(GenericResponseDTO.builder().message("Error: " + e.getMessage())
					.objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<GenericResponseDTO> obtenerEtiquetas() {
		try {
			GenericResponseDTO genericResponse = webClient.post().uri("/generarDocumento/obtenerEtiquetas")
					.body(Mono.just(this.generarDocumento), GenerarDocumentoServiceRQ.class).retrieve()
					.bodyToMono(GenericResponseDTO.class).block();
			if (genericResponse.getStatusCode() == HttpStatus.OK.value()) {
				genericResponse.setObjectResponse(
						DocumentoHelper.transformarTagsToAtributos(genericResponse.getObjectResponse(), Boolean.TRUE));
				return new ResponseEntity<>(genericResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(GenericResponseDTO.builder().message("Error: " + e.getMessage())
					.objectResponse(null).statusCode(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
		}

	}

}
