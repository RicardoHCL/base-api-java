package br.com.api.exceptions.base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.api.exceptions.AuthenticationJwtException;
import br.com.api.exceptions.CustomException;
import br.com.api.exceptions.ValidationException;
import br.com.api.utils.DataUtils;

@RestController
@ControllerAdvice
public class ExceptionMessageCustom extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> rendersExceptions(Exception ex, WebRequest request) {
		ex.printStackTrace();
		ExceptionResponse exceptionResponse = new ExceptionResponse(DataUtils.getStringComDataHoraAtual(),
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ex.printStackTrace();
		String mensagemErro = this.getMensagemSimplificadaArgumentNotValid(ex.getMessage());

		ExceptionResponse exceptionResponse = new ExceptionResponse(DataUtils.getStringComDataHoraAtual(), mensagemErro,
				request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CustomException.class)
	public final ResponseEntity<ExceptionResponse> customException(CustomException ex, WebRequest request) {
		ex.printStackTrace();
		ExceptionResponse exceptionResponse = new ExceptionResponse(DataUtils.getStringComDataHoraAtual(),
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationJwtException.class)
	public final ResponseEntity<ExceptionResponse> AuthenticationJwtException(AuthenticationJwtException ex,
			WebRequest request) {
		ex.printStackTrace();
		ExceptionResponse exceptionResponse = new ExceptionResponse(DataUtils.getStringComDataHoraAtual(),
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidationException.class)
	public final ResponseEntity<ExceptionResponse> validationException(ValidationException ex, WebRequest request) {
		ex.printStackTrace();
		ExceptionResponse exceptionResponse = new ExceptionResponse(DataUtils.getStringComDataHoraAtual(),
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Metodos Auxiliares
	 */

	private String getMensagemSimplificadaArgumentNotValid(String mensagemCompleta) {

		Integer indexInicial = mensagemCompleta.indexOf("]]; default message [") + 21;
		Integer indexFinal = mensagemCompleta.lastIndexOf("]") - 1;
		String mensagemResumida = mensagemCompleta.substring(indexInicial, indexFinal);

		return mensagemResumida;
	}
}