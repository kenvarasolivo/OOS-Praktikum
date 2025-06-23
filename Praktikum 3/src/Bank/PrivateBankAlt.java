package Bank;

import Bank.Exceptions.*;

import java.util.*;

public class PrivateBankAlt implements Bank{

    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    //Setter
    public void setName (String name){
        this.name = name;
    }

    public void setIncomingInterest (double incomingInterest) throws TransactionAttributeException{
        if (incomingInterest < 0 || incomingInterest > 1) {
            throw new TransactionAttributeException("incoming interest must be between 0 and 1");
        }
        this.incomingInterest = incomingInterest;
        if(!accountsToTransactions.isEmpty()) {
            for(String account : accountsToTransactions.keySet()) {
                if(accountsToTransactions.get(account) != null) {
                    for(Transaction transaction : accountsToTransactions.get(account)) {
                        if(transaction instanceof Payment payment) {
                            payment.setIncomingInterest(incomingInterest);
                        }
                    }
                }
            }
        }
    }

    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException{
        if (outgoingInterest < 0 || outgoingInterest > 1) {
            throw new TransactionAttributeException("outgoing interest must be between 0 and 1");
        }
        this.outgoingInterest = outgoingInterest;
        if(!accountsToTransactions.isEmpty()) {
            for(String account : accountsToTransactions.keySet()) {
                if(accountsToTransactions.get(account) != null) {
                    for(Transaction transaction : accountsToTransactions.get(account)) {
                        if(transaction instanceof Payment payment) {
                            payment.setOutgoingInterest(outgoingInterest);
                        }
                    }
                }
            }
        }
    }

    //Getter
    public String getName (){
        return this.name;
    }

    public double getIncomingInterest(){
        return this.incomingInterest;
    }

    public double getOutgoingInterest(){
        return this.outgoingInterest;
    }

    //Konstruktor
    public PrivateBankAlt (String Name, double IncomingInterest, double OutgoingInterest) throws TransactionAttributeException{
        this.name = Name;
        this.setIncomingInterest(incomingInterest);
        this.setOutgoingInterest(outgoingInterest);
    }

    //Copy-Konstruktor
    public PrivateBankAlt(PrivateBankAlt other) throws TransactionAttributeException {
        this(other.getName(), other.getIncomingInterest(), other.getOutgoingInterest());
    }

    //ToString
    @Override
    public String toString(){
        String result = "Name :"+this.name+
                "\nIncoming interest: "+this.incomingInterest+
                "\nOutgoing interest: "+this.outgoingInterest;
        Set<String> keys = accountsToTransactions.keySet();
        for(String key:keys){
            result += "\n" + key + " => \n";
            List<Transaction> Lists = accountsToTransactions.get(key);
            result += "[\n";
            for(Transaction transaction:Lists)
                result += transaction;
            result += "]\n";
        }
        return result;
    }

    //equals
    @Override
    public boolean equals(Object obj){
        if(this==obj)
            return true;
        if(obj instanceof PrivateBankAlt privateBank){
            if(this.name==privateBank.name && this.incomingInterest==privateBank.incomingInterest && this.outgoingInterest==privateBank.outgoingInterest){
                return true;
            }else
                return false;
        }else
            return false;
    }

    // Bank Implementation

    /**
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException{
        if(accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException("Account with the name "+account+" in the bank"+name+" already exists!");
        else {
            accountsToTransactions.put(account, List.of());
            System.out.println("Account with the name " + account + " has succesfully created in the bank " + name);
        }
    }

    /**
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException
     * @throws TransactionAlreadyExistException
     * @throws TransactionAttributeException
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException,TransactionAlreadyExistException,TransactionAttributeException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account with the name " + account + " in the bank" + name + " already exists!");
        } else {
            for (Transaction tr : transactions) {
                if (accountsToTransactions.containsKey(account) && accountsToTransactions.get(account).contains(transactions)) {
                    throw new TransactionAlreadyExistException("Duplicate transaction can not be added to the account!");
                } else {
                    if (tr instanceof Payment payment) {
                        setIncomingInterest(payment.getIncomingInterest());
                        setOutgoingInterest(payment.getOutgoingInterest());
                    }
                }
            }
            accountsToTransactions.put(account, transactions);
            System.out.println("Account with the name " + account + " and its transaction has succesfully created in the bank " + name);
        }
    }

    /**
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException
     * @throws AccountDoesNotExistException
     * @throws TransactionAttributeException
     */
    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException{
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Account with the name "+account+" in the bank"+name+" does not exist!");
        }else {
            if(accountsToTransactions.get(account).contains(transaction)){
                throw new TransactionAlreadyExistException("This transaction is already exist in the account "+account);
            }else{
                if(transaction instanceof Payment payment) {
                    payment.setIncomingInterest(incomingInterest);
                    payment.setOutgoingInterest(outgoingInterest);
                }
                accountsToTransactions.get(account).add(transaction);
                System.out.println("Transaction successfully added");

            }
        }
    }


    /**
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException
     * @throws TransactionDoesNotExistException
     */
    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException{
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Account with the name "+account+" in the bank"+name+" does not exist!");
        }else {
            if(!accountsToTransactions.get(account).contains(transaction)){
                throw new TransactionDoesNotExistException("This transaction does not exist in the account "+account);
            }else{
                accountsToTransactions.get(account).remove(transaction);
                System.out.println("Transaction successfully removed");
            }
        }
    }

    /**
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * @return
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction){
        if(accountsToTransactions.containsKey(account)){
            if(accountsToTransactions.get(account).contains(transaction))
                return true;
            else
                return false;
        }else
            return false;
    }

    /**
     *
     * @param account the selected account
     * @return
     */
    @Override
    public double getAccountBalance(String account){
        double balance=0;
        List<Transaction> Lists = accountsToTransactions.get(account);
        for(Transaction tr:Lists){
            if(tr instanceof Transfer transfer && transfer.getSender()==account)
                balance-=tr.calculate();
            else
                balance += tr.calculate();
        }
        return balance;
    }

    /**
     *
     * @param account the selected account
     * @return
     */
    @Override
    public List<Transaction> getTransactions(String account){
        return accountsToTransactions.get(account);
    }

    /**
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc){
        List<Transaction> sorted = new ArrayList<>(accountsToTransactions.get(account));
        if(asc)
            sorted.sort(Comparator.comparing(Transaction::calculate));
        else
            sorted.sort(Comparator.comparing(Transaction::calculate).reversed());
        return sorted;
    }

    /**
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive){
        List<Transaction> type = new ArrayList<>();
        List<Transaction> Lists = accountsToTransactions.get(account);
        for(Transaction tr : Lists){
            if(positive && tr.calculate()>=0){
                type.add(tr);
            }else if(!positive && tr.calculate()<0){
                type.add(tr);
            }
        }
        return type;
    }
}
