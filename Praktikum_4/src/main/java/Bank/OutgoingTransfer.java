package Bank;

import Bank.Exceptions.TransactionAlreadyExistException;
import Bank.Exceptions.TransactionAttributeException;

public class OutgoingTransfer extends Transfer{
    /**
     *
     * @param date
     * @param desc
     * @param amount
     */
    public OutgoingTransfer(String date,  double amount,String desc) throws TransactionAlreadyExistException, TransactionAttributeException {
        super(date,amount,desc);
    }

    /**
     *
     * @param date
     * @param amount
     * @param desc
     * @param send
     * @param rec
     */
    public OutgoingTransfer(String date, double amount, String desc, String send, String rec) throws TransactionAlreadyExistException, TransactionAttributeException {
        super(date,amount,desc,send,rec);
    }

    public OutgoingTransfer(IncomingTransfer other) throws TransactionAlreadyExistException, TransactionAttributeException {
        super(other);
    }

    /**
     *
     * @return
     */
    @Override
    public double calculate(){
        return - super.calculate();
    }
}