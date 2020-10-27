package KG.Neobis.FMS.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ResourceNotFoundExceptions extends RuntimeException {

    public ResourceNotFoundExceptions() {
    }

    public ResourceNotFoundExceptions(String str){
        super(str);
    }

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundExceptions.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String ResourceNotFoundHandler (ResourceNotFoundExceptions ex) {
        return ex.getMessage();
    }
}