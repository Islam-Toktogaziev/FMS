package KG.Neobis.FMS.Exceptions;

import KG.Neobis.FMS.dto.ResponseMessage;

public class ResourceNotFoundExceptions extends RuntimeException {

    private ResponseMessage responseMessage;

    public ResourceNotFoundExceptions() {
    }

    public ResourceNotFoundExceptions(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }
}