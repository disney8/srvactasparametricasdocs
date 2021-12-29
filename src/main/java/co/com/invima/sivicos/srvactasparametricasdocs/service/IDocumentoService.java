package co.com.invima.sivicos.srvactasparametricasdocs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.actasparametricas.BloquePlantillaDTO;
import co.com.invima.sivicos.srvactasparametricasdocs.service.dto.ArchivoResponseDTO;

public interface IDocumentoService {

	 ResponseEntity<GenericResponseDTO>  obtenerVariablesDocumento(final 	ArchivoResponseDTO documento);
	
	
	 ResponseEntity<GenericResponseDTO>  actualizarDatosDocumento(final List<BloquePlantillaDTO> lstBloques, final Boolean indVistaPrevia);
}
