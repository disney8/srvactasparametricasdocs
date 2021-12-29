package co.com.invima.sivicos.srvactasparametricasdocs.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import co.com.invima.sivicos.srvactasparametricasdocs.enums.ExtensionesArchivosEnum;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

public class Utils {
    
	public static String converWordPdf(final byte[] pArchivo) {
		
		InputStream is = null;
		XWPFDocument document = null;
		OutputStream out = null;
		ByteArrayOutputStream bos = null;
		String documentoFinal =null;
		try {
			long start = System.currentTimeMillis();
			is = new ByteArrayInputStream(pArchivo);

			document = new XWPFDocument(is);
			PdfOptions options = PdfOptions.create();

			bos = new ByteArrayOutputStream();
			PdfConverter.getInstance().convert(document, bos, options);

			document.write(bos);
			Base64 base64 = new Base64();
			
			documentoFinal =base64.encodeToString(bos.toByteArray());
			System.out.println("Documento convertido a pdf  Tiempo de la conversión MS: " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (document != null) {
					document.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (out != null) {
					out.close();
				}
				if (is != null) {
					is.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return documentoFinal;
	}
	
	 public String getFileExtension(String nombre) {
	        String tipoContenido = "";
	        String extension = "";
	        try {
	            Path path = new File(nombre).toPath();
	            tipoContenido = Files.probeContentType(path);
	            ExtensionesArchivosEnum extensionArchivo = ExtensionesArchivosEnum.obtenerExtensionPorTipoContenido(tipoContenido);
	            extension = extensionArchivo.getExtension();
	        } catch (NullPointerException | IOException e) {
                     e.printStackTrace();
	        }
	        return extension;

	    }

}
