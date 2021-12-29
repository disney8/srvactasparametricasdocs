package co.com.invima.sivicos.srvactasparametricasdocs.service;

import org.springframework.http.ResponseEntity;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;

public interface IPlantillaDocumento {
	
	public ResponseEntity<GenericResponseDTO>  obtenerDocumento(final Integer idPlantilla);

}
