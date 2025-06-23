package Bank;

public class Transfer {
    private String date; // Datum im Format DD.MM.YYYY
    private double amount; //Geldmenge einer Überweisung, muss Positive sein
    private String description; // Zusätzliche Beschreibung des Vorgangs
    private String sender;  // Der Name des Absenders
    private String recipient; // Der Name des Empfängers

    private boolean validity = true; //überprüft Gültigkeit aller Wert

    //Unten finden Sie Getter und Setter für alle Attribute
    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if(amount >= 0) {
            this.amount = amount;
        } else {
            System.out.println(" Amount can't be negative ");
            validity = false;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    //Konstruktor für die Attribute date, amount und description
    public Transfer(String date , double amount, String description){
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    //Konstruktor für alle Attribute
    public Transfer(String date2, double amount2, String description2, String sender, String recipient){
        this(date2,amount2,description2);
        setSender(sender);
        setRecipient(recipient);
    }

    //Copy-Konstruktor
    public Transfer(Transfer other){
        this(other.date, other.amount, other.description, other.sender, other.recipient);
    }

    //Funktion für die Objekte der Klasse auszudrücken.
    public void printObject(){
        if(validity) { //Überprüft Gültigkeit
            System.out.println(
                    " Date: " + getDate() + "\n" +
                            " Amount: " + getAmount() + "\n" +
                            " Description: " + getDescription() + "\n" +
                            " Sender: " + getSender() + "\n" +
                            " Recipient: " + getRecipient() + "\n"
            );
        }
        else {
            System.out.println(
                    " Error, input not correct \n"
            );
        }
    }
}
