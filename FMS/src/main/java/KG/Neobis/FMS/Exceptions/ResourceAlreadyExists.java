package KG.Neobis.FMS.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ResourceAlreadyExists extends RuntimeException {

    public ResourceAlreadyExists() {
    }

    public ResourceAlreadyExists(String message) {
        super(message);
    }

    @ResponseBody
    @ExceptionHandler(ResourceAlreadyExists.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String ResourceNotFoundHandler (ResourceNotFoundExceptions ex) {
        return ex.getMessage();
    }
}
