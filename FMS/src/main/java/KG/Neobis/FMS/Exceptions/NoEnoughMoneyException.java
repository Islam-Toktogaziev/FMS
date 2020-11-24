package KG.Neobis.FMS.Exceptions;

public class NoEnoughMoneyException extends RuntimeException {

    public NoEnoughMoneyException() {
    }

    public NoEnoughMoneyException(String message) {
        super(message);
    }
}
