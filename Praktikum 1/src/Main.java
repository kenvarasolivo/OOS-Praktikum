import Bank.Transfer;
import Bank.Payment;

public class Main {
    public static void main(String[] args) {
        // Testfälle für die Payment-Klasse
        // Test mit dem Konstruktor (date, amount, description)
        Payment payment1 = new Payment("21.10.2024", 500, "Gehalt");
        payment1.printObject();

        // Test mit dem Konstruktor für alle Attribute
        Payment payment2 = new Payment("22.10.2024", -200, "Miete", 0.5, 0.2);
        payment2.printObject();

        // Test des Copy-Konstruktor
        Payment payment3 = new Payment(payment2);
        payment3.printObject();

        payment2.setAmount(-100);
        payment3.printObject();

        // Test mit ungültigen Zinsen
        Payment payment4 = new Payment("23.10.2024", -50, "Supermarkt", 1.5, -0.5);
        payment4.printObject();


        // Testfälle für die Transfer-Klasse
        // Test mit dem Konstruktor (date, amount, description)
        Transfer transfer1 = new Transfer("21.10.2024", 1000, "Transfer to Savings account");
        transfer1.printObject();

        // Test mit dem Konstruktor für alle Attribute
        Transfer transfer2 = new Transfer("22.10.2024", 1500, "Transfer to a friend", "Ken", "Vara");
        transfer2.printObject();

        // Test des Copy-Konstruktor
        Transfer transfer3 = new Transfer(transfer2);
        transfer3.printObject();

        // Test mit negativem Betrag
        Transfer transfer4 = new Transfer("23.10.2024", -1500, "Transfer to a friend", "Vara", "Ken");
        transfer4.printObject();
    }
}