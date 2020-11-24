package KG.Neobis.FMS.Exceptions;

import KG.Neobis.FMS.dto.ResponseMessage;

public class Exception extends RuntimeException {

    private ResponseMessage responseMessage;

    public Exception(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }
}
