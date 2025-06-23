import Bank.*;
import Bank.Exceptions.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException{
        /*
        //Praktikum 1
        // Testfälle für die Payment-Klasse
        // Test mit dem Konstruktor (date, amount, description)
        Payment payment1 = new Payment("21.10.2024", 500, "Gehalt");
        System.out.println("Konstruktor des Payment (3 parameter) Test: \n" + payment1 + "\n");

        // Test mit dem Konstruktor für alle Attribute
        Payment payment2 = new Payment("22.10.2024", -200, "Miete", 0.5, 0.2);
        System.out.println("Konstruktor des Payment (5 parameter) Test: \n" + payment2 + "\n");

        // Test des Copy-Konstruktor
        Payment payment3 = new Payment(payment2);
        System.out.println("Copy-Konstruktor des Payment Test: \n" + payment3 + "\n");

        // Test mit ungültigen Zinsen
        try {
            Payment payment4 = new Payment("23.10.2024", -50, "Supermarkt", 1.5, -0.5);
        } catch (IncomingException | OutgoingException fail) {
            System.out.println(fail);
        }

        // Testfälle für die Transfer-Klasse
        // Test mit dem Konstruktor (date, amount, description)
        Transfer transfer1 = new Transfer("21.10.2024", 1000, "Transfer to Savings account");
        System.out.println("\n Konstruktor des Transfer (3 parameter) Test: \n" + transfer1 + "\n");

        // Test mit dem Konstruktor für alle Attribute
        Transfer transfer2 = new Transfer("22.10.2024", 1500, "Transfer to a friend", "Ken", "Vara");
        System.out.println("\n Konstruktor des Transfer (5 parameter) Test: \n" + transfer2 + "\n");

        // Test des Copy-Konstruktor
        Transfer transfer3 = new Transfer(transfer2);
        System.out.println("Copy-Konstruktor des Transfer Test: \n" + transfer3 + "\n");

        // Test mit negativem Betrag
        try {
            Transfer transfer4 = new Transfer("23.10.2024", -1500, "Transfer to a friend", "Vara", "Ken");
        }catch (TransactionAttributeException fail) {
                System.out.println(fail);
        }


        // Praktikum 2
        // Testfälle für calculate Payment
        Payment paymentIn = new Payment("22.10.2024", 200, "Miete", 0.5, 0.2);
        System.out.println("Payment In: " + "\n" + paymentIn);
        System.out.println("Calculated Amount In: " + paymentIn.calculate() + "\n");

        Payment paymentOut = new Payment("22.10.2024", -200, "Miete", 0.5, 0.2);
        System.out.println("Payment Out: " + "\n" + paymentOut);
        System.out.println("Calculated Amount Out: " + paymentOut.calculate() + "\n");

        // Testfälle für calculate Transfer
        Transfer transfer = new Transfer("22.10.2024", 1500, "Transfer to a friend", "Ken", "Vara");
        System.out.println("Transfer: " + "\n" + transfer);
        System.out.println("Calculated Transfer Amount: " + transfer.calculate() + "\n");

        // Test für Equals and toString
        System.out.println("Payment In equals Payment Out: " + paymentIn.equals(paymentOut)+ "\n");

        System.out.println("Payment In toString: " + "\n" + paymentIn.toString() + "\n");
        System.out.println("Transfer toString: " + transfer.toString() + "\n");
         */

        //Praktikum 3
        //Test für Bank
        PrivateBank privateBank = new PrivateBank("bank1", 0.15,0.1);
        privateBank.createAccount("Timothy", List.of(
                new OutgoingTransfer("09.11.2024",520,"this is Transfer","Timothy","Hanif"),
                new Payment("10.11.2024",1200,"this is deposit",0.15,0.1),
                new Payment("11.11.2024",-1100,"this is deposit",0.12,0.3),
                new IncomingTransfer("12.11.2024",450,"this is Transfer","Filbert","Timothy")
        ));

        privateBank.createAccount("Garrie", List.of(
                new IncomingTransfer("09.11.2024",560,"this is Transfer","Billy","Garrie"),
                new Payment("10.11.2024",1255,"this is deposit",0.15,0.1),
                new Payment("11.11.2024",-1350,"this is deposit",0.12,0.3),
                new OutgoingTransfer("12.11.2024",520,"this is Transfer","Garrie","Hanif")
        ));

        System.out.println(privateBank.toString());

        PrivateBank privateBank2 = new PrivateBank("bank1", 0.15,0.1);

        privateBank2.createAccount("Timothy", List.of(
                new Transfer("9.11.2024",520,"this is Transfer","Sheifer","Timothy"),
                new Payment("11.11.2024",1200,"this is deposit",0.15,0.1)

        ));

        privateBank2.createAccount("Fibert", List.of(
                new Transfer("10.11.2024",560,"this is Transfer","Filbert","Billy"),
                new Transfer("11.11.2024",520,"this is Transfer","Garrie","Filbert")
        ));

        PrivateBank privateBank3=new PrivateBank("bank1", 0.15,0.1);

        privateBank3.createAccount("Timothy", List.of(
                new Transfer("9.11.2024",520,"this is Transfer","Sheifer","Timothy"),
                new Payment("11.11.2024",1200,"this is deposit",0.15,0.1)
        ));

        privateBank3.createAccount("Fibert", List.of(
                new Transfer("10.11.2024",560,"this is Transfer","Filbert","Billy"),
                new Transfer("11.11.2024",520,"this is Transfer","Garrie","Filbert")
        ));

        System.out.println(privateBank.equals(privateBank2));
        System.out.println(privateBank2.equals(privateBank3));

        //Alt
        PrivateBankAlt privateBankAlt = new PrivateBankAlt("bank1", 0.15, 0.1);
        privateBankAlt.createAccount("Jonathan", List.of(
                new Transfer("02.08.2024", 600, "this is Transfer", "Jonathan", "Sophia"),
                new Payment("01.02.2024", 1300, "this is deposit", 0.15, 0.1),
                new Payment("01.02.2024", -1400, "this is deposit", 0.12, 0.3),
                new Transfer("02.08.2024", 950, "this is Transfer", "Lily", "Jonathan")
        ));

        privateBankAlt.createAccount("Lily", List.of(
                new Transfer("02.08.2024", 550, "this is Transfer", "Lily", "Sophia"),
                new Payment("01.02.2024", 1400, "this is deposit", 0.15, 0.1),
                new Payment("01.02.2024", -1300, "this is deposit", 0.12, 0.3),
                new Transfer("02.08.2024", 450, "this is Transfer", "Jonathan", "Lily")
        ));

        System.out.println(privateBankAlt);

        try {
            privateBankAlt.createAccount("Lily", List.of(new Transfer("02.08.2024", 550, "this is Transfer", "Jonathan", "Lily")));
        } catch (AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException fail) {
            System.out.println(fail);
        }

        try {
            privateBankAlt.addTransaction("Jack", new Transfer("02.08.2024", 500, "this is Transfer", "Jonathan", "Lily"));
        } catch (TransactionAlreadyExistException | AccountDoesNotExistException | TransactionAttributeException fail) {
            System.out.println(fail);
        }

        //Incomingexception
        try {
            privateBankAlt.addTransaction("Lily", new Payment("01.02.2024", 1450, "this is deposit", 1.5, 0.3));
        } catch (TransactionAlreadyExistException | AccountDoesNotExistException | TransactionAttributeException fail) {
            System.out.println(fail);
        }

        //Outgoingexception
        try {
            privateBankAlt.addTransaction("Lily", new Payment("01.02.2024", 1450, "this is deposit", 0.9, 1.6));
        } catch (TransactionAlreadyExistException | AccountDoesNotExistException | TransactionAttributeException fail) {
            System.out.println(fail);
        }

        //Transaction already exist
        try {
            privateBankAlt.addTransaction("Lily", new Transfer("02.08.2024", 550, "this is Transfer", "Lily", "Sophia"));
        } catch (TransactionAlreadyExistException | AccountDoesNotExistException | TransactionAttributeException fail) {
            System.out.println(fail);
        }

        //Transaction Attribut exception
        try {
            privateBankAlt.addTransaction("Lily", new Transfer("02.08.2024", -5000, "this is Transfer", "Lily", "Sophia"));
        } catch (TransactionAlreadyExistException | AccountDoesNotExistException | TransactionAttributeException fail) {
            System.out.println(fail);
        }

        //transaction does not exist
        try {
            privateBankAlt.removeTransaction("Lily", new Payment("01.02.2024", 50, "this is deposit", 0.12, 0.3));
        } catch (AccountDoesNotExistException | TransactionDoesNotExistException fail) {
            System.out.println(fail);
        }

        System.out.println(privateBankAlt.getTransactionsSorted("Lily", true));
        System.out.println(privateBankAlt.getTransactionsSorted("Lily", false));
        System.out.println(privateBankAlt.getTransactionsByType("Lily", true));
        System.out.println(privateBankAlt.getTransactionsByType("Lily", false));

        System.out.println(privateBankAlt.containsTransaction("Lily", new Payment("01.02.2024", -1300, "this is deposit", 0.12, 0.3)));
        System.out.println(privateBankAlt.containsTransaction("Lily", new Payment("01.02.2024", 1400, "this is deposit", 0.15, 0.1)));
        System.out.println(privateBankAlt.containsTransaction("Sophia", new Payment("01.02.2024", -1300, "this is deposit", 0.12, 0.3)));

    }
}