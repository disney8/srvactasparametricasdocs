package co.com.invima.sivicos.srvactasparametricasdocs.service.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArchivoResponseDTO implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String nombreArchivo;
	private String contenido;

}

