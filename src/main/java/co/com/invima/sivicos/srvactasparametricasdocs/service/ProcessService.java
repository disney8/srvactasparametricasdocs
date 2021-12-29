package co.com.invima.sivicos.srvactasparametricasdocs.service;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
    User:Eduardo Noel<enoel@soaint.com>
    Date: 30/8/21
    Time: 19:50
*/
public interface ProcessService {
	GenericResponseDTO getListaEtiquetas(String base64) throws IOException;
    List<String> processXlsx(File excel);
    List<String> processXls(File excel);

    GenericResponseDTO getPDF(Map<String,Object> datos) throws IOException;
    String processXlsxToPDF(Map<String,Object> datos) throws IOException;
    String processXlsToPDF(Map<String,Object> datos) throws IOException;

    String detectMimeType(String base64) throws IOException;
    File constructFile(String base64) throws IOException;
    void replaceTags(Map<String, String> valores, List<String> llaves, Iterator<Row> rowIterator);
    String base64PDF(File output) throws IOException;
}
