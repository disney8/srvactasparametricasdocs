package co.com.invima.sivicos.srvactasparametricasdocs.service;

import org.springframework.http.ResponseEntity;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.actasparametricas.DatosPlantillaDTO;

public interface IPlantillaDatosService {
	
	public ResponseEntity<GenericResponseDTO>  guardarDatosPlantilla(final DatosPlantillaDTO datosFinales);
}
