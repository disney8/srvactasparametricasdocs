package co.com.invima.sivicos.srvactasparametricasdocs.config;

import co.com.invima.canonicalmodelsivico.dtosivico.GenericResponseDTO;
import co.com.invima.canonicalmodelsivico.dtosivico.config.DtoSivicoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

/*
    User:Eduardo Noel<enoel@soaint.com>
    Date: 9/7/21
    Time: 1:47
*/
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalAPIErrorHandler extends ResponseEntityExceptionHandler {

    private final DtoSivicoProperties sivicoProperties;

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GenericResponseDTO> errorPeticion(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponseDTO.builder()
                        .objectResponse(ex.getMessage())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(sivicoProperties.getFormatoincorrecto())
                        .build());
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<GenericResponseDTO> errorNoEncontrado(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericResponseDTO.builder()
                        .objectResponse(ex.getMessage())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(sivicoProperties.getNotfound())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GenericResponseDTO> internalError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenericResponseDTO.builder()
                        .objectResponse(ex.getMessage())
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(sivicoProperties.getSevero())
                        .build());
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponseDTO.builder()
                        .objectResponse(ex.getMessage())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(sivicoProperties.getFormatoincorrecto())
                        .build());
    }
}
