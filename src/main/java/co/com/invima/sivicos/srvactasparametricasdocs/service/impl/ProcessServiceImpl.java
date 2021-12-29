package co.com.invima.sivicos.srvactasparametricasdocs.service.impl;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.config.DtoSivicoProperties;
import co.com.invima.sivicos.srvactasparametricasdocs.service.ProcessService;
import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/*
    User:Eduardo Noel<enoel@soaint.com>
    Date: 30/8/21
    Time: 19:50
*/
@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessServiceImpl implements ProcessService {

    private final DtoSivicoProperties sivicoProperties;
    private final Pattern p = Pattern.compile("\\$\\{[a-z0-9]+}");
    private static final String CONTENT_TYPE = "application/x-tika-msoffice";
    private static final String BASE64 = "base64";

    @Override
    public GenericResponseDTO getListaEtiquetas(String base64) throws IOException {

        List<String> lista;

        String uuid = UUID.randomUUID().toString();

        File tempFile = new File(uuid);

        String mimeType = detectMimeType(base64);

        if (StringUtils.equals(CONTENT_TYPE,mimeType)){

            byte[] bytes = Base64.getDecoder().decode(base64);
            FileUtils.writeByteArrayToFile(tempFile,bytes);

            lista = processXls(tempFile);

        } else {

            byte[] bytes = Base64.getDecoder().decode(base64);
            FileUtils.writeByteArrayToFile(tempFile,bytes);

            lista = processXlsx(tempFile);

        }

        FileUtils.forceDelete(tempFile);

        return GenericResponseDTO.builder()
                .message(sivicoProperties.getListado())
                .objectResponse(lista)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @Override
    public List<String> processXlsx(File excel) {

        List<String> lista = new ArrayList<>();

        try(XSSFWorkbook wb = new XSSFWorkbook(excel)) {

            XSSFExcelExtractor we = new XSSFExcelExtractor(wb);

            Matcher m = p.matcher(we.getText());

            while (m.find()) {

                int longitud = m.group().length();

                String volatil = m.group().substring(2, longitud - 1);

                lista.add(volatil);

            }

        } catch (IOException | InvalidFormatException ex){

            log.error(ex.getMessage());

        }

        return lista;
    }

    @Override
    public List<String> processXls(File excel) {

        List<String> lista = new ArrayList<>();

        try(HSSFWorkbook wb2 = new HSSFWorkbook(FileUtils.openInputStream(excel))) {

            ExcelExtractor we = new ExcelExtractor(wb2);

            Matcher m = p.matcher(we.getText());

            while (m.find()) {

                int longitud = m.group().length();

                String volatil = m.group().substring(2, longitud - 1);

                lista.add(volatil);
            }

        } catch (IOException ex){

            log.error(ex.getMessage());

        }

        return lista;

    }

    @Override
    public GenericResponseDTO getPDF(Map<String, Object> datos) throws IOException {

        String base64 = (String) datos.get(BASE64);
        String result;

        String mimeType = detectMimeType(base64);

        if (StringUtils.equals(CONTENT_TYPE,mimeType)){

            result = processXlsToPDF(datos);

        } else {

            result = processXlsxToPDF(datos);

        }

        return GenericResponseDTO.builder()
                .message(sivicoProperties.getListado())
                .objectResponse(result)
                .statusCode(HttpStatus.OK.value())
                .build();

    }

    @Override
    public File constructFile(String base64) throws IOException {

        String uuid = UUID.randomUUID().toString();

        File fichero = new File(uuid);

        byte[] bytes = Base64.getDecoder().decode(base64);
        FileUtils.writeByteArrayToFile(fichero,bytes);

        return fichero;

    }

    @Override
    public String processXlsxToPDF(Map<String,Object> datos) throws IOException {

        File fichero = constructFile((String) datos.get(BASE64));

        File ficheroSalida = new File(UUID.randomUUID().toString());

        Map<String, String> valores = (Map<String, String>)datos.get("tags");

        List<String> llaves = new ArrayList<>(valores.keySet());

        String base64PDF = "";

        try(XSSFWorkbook workBook = new XSSFWorkbook(fichero)) {

            int n = workBook.getNumberOfSheets();

            IntStream.range(0, n).parallel()
                    .forEach(idx -> {

                        XSSFSheet sheet = workBook.getSheetAt(idx);

                        replaceTags(valores, llaves, sheet.rowIterator());

                    });

            workBook.write(new FileOutputStream(ficheroSalida));
            base64PDF = base64PDF(ficheroSalida);

            FileUtils.forceDelete(fichero);
            FileUtils.forceDelete(ficheroSalida);


        } catch (IOException | InvalidFormatException ex){

            log.error(ex.getMessage());

        }

        return base64PDF;
    }

    @Override
    public String processXlsToPDF(Map<String,Object> datos) throws IOException {

        File fichero = constructFile((String) datos.get(BASE64));

        File ficheroSalida = new File(UUID.randomUUID().toString());

        Map<String, String> valores = (Map<String, String>)datos.get("tags");

        List<String> llaves = new ArrayList<>(valores.keySet());

        String base64PDF;

        try(HSSFWorkbook workBook = new HSSFWorkbook(FileUtils.openInputStream(fichero))) {

            int n = workBook.getNumberOfSheets();

            IntStream.range(0, n).parallel()
                    .forEach(idx -> {

                        HSSFSheet sheet = workBook.getSheetAt(idx);

                        replaceTags(valores, llaves, sheet.rowIterator());

                    });


            workBook.write(new FileOutputStream(ficheroSalida));
            base64PDF = base64PDF(ficheroSalida);

            FileUtils.forceDelete(fichero);
            FileUtils.forceDelete(ficheroSalida);

        }

        return base64PDF;
    }

    @Override
    public void replaceTags(Map<String, String> valores, List<String> llaves, Iterator<Row> rowIterator) {

        Stream<Row> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(rowIterator, Spliterator.ORDERED), true);

        stream.parallel().forEach(x -> {

            Stream<Cell> cellStream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(x.cellIterator(), Spliterator.ORDERED), true);

            cellStream.parallel().forEach(x1 -> {

                Matcher m = p.matcher(x1.toString());

                if (m.find()) {

                    int longitud = x1.toString().length();

                    String volatil = x1.toString().substring(2, longitud - 1);

                    Optional<String> temp = llaves.stream().parallel()
                            .filter(x2 -> StringUtils.equals(volatil, x2))
                            .findFirst();

                    temp.ifPresent(llave -> x1.setCellValue(valores.get(llave)));
                }

            });

        });

    }

    @Override
    public String detectMimeType(String base64) throws IOException {

        String uuid = UUID.randomUUID().toString();

        byte[] bytes = Base64.getDecoder().decode(base64);

        File tempFile = new File(uuid);

        FileUtils.writeByteArrayToFile(tempFile,bytes);

        Tika tika = new Tika();

        String result = tika.detect(tempFile);

        FileUtils.forceDelete(tempFile);

        return result;

    }

    @Override
    public String base64PDF(File output) throws IOException {

        String base64PDF;
        Workbook wb = new Workbook();
        wb.loadFromFile(output.getName(), true);
        wb.getConverterSetting().setSheetFitToWidth(true);

        String pdf = UUID.randomUUID().toString();

        wb.saveToFile(pdf, FileFormat.PDF);

        base64PDF = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(new File(pdf)));

        FileUtils.forceDelete(new File(pdf));

        return base64PDF;

    }


}
