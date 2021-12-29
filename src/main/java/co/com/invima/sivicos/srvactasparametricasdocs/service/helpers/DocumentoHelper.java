package co.com.invima.sivicos.srvactasparametricasdocs.service.helpers;

import java.util.ArrayList;
import java.util.List;

import co.com.invima.canonicalmodelsivico.dtosivico.actasparametricas.AtributoPlantillaDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.actasparametricas.BloquePlantillaDTO;
import co.com.invima.sivicos.srvactasparametricasdocs.rq.GenerarDocumentoServiceRQ;


public class DocumentoHelper {
	
	public static GenerarDocumentoServiceRQ asignarVariables(final List<BloquePlantillaDTO> lstBloques){
		GenerarDocumentoServiceRQ documento = new GenerarDocumentoServiceRQ();
		
		for(int i =0; i<lstBloques.size();i++) {
			for(int j =0; j<lstBloques.get(i).getAtributosPlantilla().size();j++) {
				documento.getTags().put(lstBloques.get(i).getAtributosPlantilla().get(j).getNombre(), lstBloques.get(i).getAtributosPlantilla().get(j).getValor());
			}
		}
		
		return documento;
	}
	
	/*@SuppressWarnings("static-access")
	public static ArchivoDTO transformarToArchivo(final String documento) {
		ArchivoDTO archivo = new ArchivoDTO();
		System.out.println("inicio transformarToArchivo  " + documento);
		 Base64 b = new Base64();
		 
		archivo.setContenido(b.decodeBase64(documento));
		archivo = Utils.converWordPdf(archivo);
		System.out.println("fin transformarToArchivo  " + archivo.getContenido());
		
		return  archivo;
	}*/
	
	public static Object transformarTagsToAtributos(final Object tags, final Boolean indDocx){
		List<AtributoPlantillaDTO> lstAtributos= new ArrayList<AtributoPlantillaDTO>();
		System.out.println("tags transformarTagsToAtributos:  " +  tags);
		@SuppressWarnings("unchecked")
		List<String> lstTags = (List<String>) tags;
		System.out.println("lstTags size " + lstTags.size());
		for(int i=0; i<lstTags.size();i++) {
			System.out.println("lstTags item: " + lstTags.get(i));
			final AtributoPlantillaDTO atributo = new AtributoPlantillaDTO();
			if(indDocx) {
			  atributo.setNombre(lstTags.get(i).substring(2, lstTags.get(i).length()-1));
			}else {
				atributo.setNombre(lstTags.get(i));
			}
			lstAtributos.add(atributo);
		}
		System.out.println("lstAtributos " +  lstAtributos.size());
		return lstAtributos;
	}
}
