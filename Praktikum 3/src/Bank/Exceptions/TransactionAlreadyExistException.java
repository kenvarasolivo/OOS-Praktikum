package Bank.Exceptions;


public class TransactionAlreadyExistException extends Exception {
    public TransactionAlreadyExistException(String message){
        super(message);
    }
}
