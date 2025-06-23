import Bank.Transaction;
import Bank.Transfer;
import Bank.Payment;

public class Main {
    public static void main(String[] args) {
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
        Payment payment4 = new Payment("23.10.2024", -50, "Supermarkt", 1.5, -0.5);

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
        Transfer transfer4 = new Transfer("23.10.2024", -1500, "Transfer to a friend", "Vara", "Ken");

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
    }
}