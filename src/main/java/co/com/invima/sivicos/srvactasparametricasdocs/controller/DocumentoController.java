package co.com.invima.sivicos.srvactasparametricasdocs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.actasparametricas.BloquePlantillaDTO;
import co.com.invima.sivicos.srvactasparametricasdocs.service.IDocumentoService;
import co.com.invima.sivicos.srvactasparametricasdocs.service.dto.ArchivoResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/plantilla/")
@CrossOrigin(origins = "*", methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT})
@Api("Controlador que permite gestionar las operaciones de documento para el proyecto Invima Sivicos")
public class DocumentoController {

	private final IDocumentoService documentoService;
	
	@Autowired
	public DocumentoController(IDocumentoService documentoService) {
		this.documentoService = documentoService;
	}
	
	@ApiOperation(value = "Obtener variables documento", notes = "Método que obtener variables documento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Se obtienen los tags", response = GenericResponseDTO.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Error inesperado del sistema")
	})
	@PostMapping("documento")
	public ResponseEntity<GenericResponseDTO> obtenerVariablesDocumento(@RequestBody ArchivoResponseDTO archivo){
		return documentoService.obtenerVariablesDocumento(archivo);
		
	}
	@ApiOperation(value = "Actualizar los datos de archivo", notes = "Método que actualiza los tags del archivo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Se actualiza los tags del archivo", response = GenericResponseDTO.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Error inesperado del sistema")
	})
	@PutMapping("documento")
	public ResponseEntity<GenericResponseDTO> actualizarDatosDocumento(@RequestBody List<BloquePlantillaDTO> lstBloques,
			@RequestParam(required = false) Boolean indVistaPrevia,
			@RequestParam(required = false) Long idVisita,
			@RequestParam(required = false) Long idPlantilla){
		System.out.println("Ingresar actualizarDatosDocumento controler");
		return documentoService.actualizarDatosDocumento(lstBloques, indVistaPrevia, idVisita, idPlantilla);
		
	}
}
