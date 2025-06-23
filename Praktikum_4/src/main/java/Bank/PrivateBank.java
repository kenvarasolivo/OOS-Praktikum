package Bank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import java.io.*;
import java.nio.file.*;
import com.google.gson.reflect.TypeToken;

import Bank.Exceptions.*;
import java.util.*;

public class PrivateBank implements Bank{

    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    public Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    private static String directoryName = "./bankData";  // The directory for storing account data

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
    public PrivateBank (String Name, double IncomingInterest, double OutgoingInterest) throws TransactionAttributeException{
        this.name = Name;
        this.setIncomingInterest(IncomingInterest);
        this.setOutgoingInterest(OutgoingInterest);
        try {
            readAccounts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Copy-Konstruktor
    public PrivateBank(PrivateBank other) throws TransactionAttributeException {
        this(other.getName(), other.getIncomingInterest(), other.getOutgoingInterest());
    }

    //ToString
    @Override
    public String toString(){
        String result = "Name :" + this.name +
                "\nIncoming Interest: " + this.incomingInterest +
                "\nOutgoing Interest: " + this.outgoingInterest;
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
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        PrivateBank other = (PrivateBank) obj;
        if(!name.equals(other.getName()) && incomingInterest != other.getIncomingInterest() && outgoingInterest != other.getOutgoingInterest()) {
            return false;
        }
        for(String account : accountsToTransactions.keySet()) {
            if(!accountsToTransactions.get(account).equals(other.accountsToTransactions.get(account))) return false;
            for(Transaction transaction : accountsToTransactions.get(account)) {
                if(!other.accountsToTransactions.get(account).contains(transaction)) return false;
            }
        }
        return true;
    }

    // Bank Implementation

    /**
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException{
        if(accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException("Account with the name "+account+" in the bank"+name+" already exists!");
        else {
            accountsToTransactions.put(account, new ArrayList<>());
            writeAccount(account);
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
            throws AccountAlreadyExistsException,TransactionAlreadyExistException,TransactionAttributeException, IOException {
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
            writeAccount(account);
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
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account does not exist: " + account);
        }
        if (accountsToTransactions.get(account).contains(transaction)) {
            throw new TransactionAlreadyExistException("Transaction already exists: " + transaction);
        }
        if (transaction instanceof Payment payment) {
            payment.setIncomingInterest(incomingInterest);
            payment.setOutgoingInterest(outgoingInterest);
        }

        accountsToTransactions.get(account).add(transaction);
        System.out.println("Transaction added: " + transaction);
        writeAccount(account);
        System.out.println("Account written to file: " + account);
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
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Account with the name "+account+" in the bank"+name+" does not exist!");
        }else {
            if(!accountsToTransactions.get(account).contains(transaction)){
                throw new TransactionDoesNotExistException("This transaction does not exist in the account "+account);
            }else{
                accountsToTransactions.get(account).remove(transaction);
                writeAccount(account);
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

    // Method to write a specific account and its transactions to a JSON file
    public void writeAccount(String account) throws IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new IOException("Account not found: " + account);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new Serializer())
                .setPrettyPrinting()
                .create();

        // Serialize transactions with custom serializer
        String jsonContent = gson.toJson(accountsToTransactions.get(account), new TypeToken<List<Transaction>>() {}.getType());

        // Write JSON to file
        File file = new File(directoryName, account + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonContent);
        }
    }

    // Method to read all accounts from files and populate the map
    public void readAccounts() throws IOException {
        File dir = new File(directoryName);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + directoryName);
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));
        if (files == null) {
            return; // No files to process
        }

        Gson gson = new GsonBuilder().registerTypeAdapter(Transaction.class, new Serializer()).create();

        for (File file : files) {
            try (Reader reader = Files.newBufferedReader(file.toPath())) {
                String accountName = file.getName().replace(".json", "");
                List<Transaction> transactions = gson.fromJson(reader, new TypeToken<List<Transaction>>() {}.getType());
                accountsToTransactions.put(accountName, transactions);
            }
        }
    }
}