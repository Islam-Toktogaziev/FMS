package KG.Neobis.FMS.Exceptions;

public class ResourceAlreadyExists extends RuntimeException {

    public ResourceAlreadyExists() {
    }

    public ResourceAlreadyExists(String message) {
        super(message);
    }

}
