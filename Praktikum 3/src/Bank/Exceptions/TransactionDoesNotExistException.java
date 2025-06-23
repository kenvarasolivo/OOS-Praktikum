package Bank.Exceptions;

public class TransactionDoesNotExistException extends Exception{
    public TransactionDoesNotExistException(String message){
        super(message);
    }
}
