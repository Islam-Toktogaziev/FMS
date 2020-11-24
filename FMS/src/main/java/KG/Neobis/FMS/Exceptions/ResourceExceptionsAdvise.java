package KG.Neobis.FMS.Exceptions;

import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ResourceExceptionsAdvise {

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundExceptions.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseMessage ResourceNotFoundHandler (ResourceNotFoundExceptions ex) {
        return ex.getResponseMessage();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    ResponseMessage ExceptionHandler (Exception ex) {
        return ex.getResponseMessage();
    }
}
