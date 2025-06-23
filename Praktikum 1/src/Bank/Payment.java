package Bank;

public class Payment {
    private String date; // Datum im Format DD.MM.YYYY
    private double amount; //Geldmenge einer Ein-/Auszahlung, kann Negative sein
    private String description; // Zusätzliche Beschreibung des Vorgangs
    private double incomingInterest; //Zinsen bei Deposit, Wert muss von 0 bis 1 sein
    private double outgoingInterest; //Zinsen bei Withdrawal, Wert muss von 0 bis 1 sein

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
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public double getIncomingInterest() {
        return incomingInterest;
    }

    public void setIncomingInterest(double IncomingInterest) {
        if (IncomingInterest >= 0 && IncomingInterest <= 1) { //Überprüft wenn es zwischen 0 und 1
            this.incomingInterest = IncomingInterest;
        }
        else {
            System.out.println(" Not a valid Incoming Interest ");

        }
    }

    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    public void setOutgoingInterest(double OutgoingInterest) {
        if (OutgoingInterest >= 0 && OutgoingInterest <= 1) { //Überprüft wenn es zwischen 0 und 1
            this.outgoingInterest = OutgoingInterest;
        }
        else {
            System.out.println(" Not a valid Outgoing Interest ");
            validity = false;
        }
    }

    //Konstruktor für die Attribute date, amount und description
    public Payment(String date, double amount, String description) {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    //Konstruktor für alle Attribute
    public Payment(String date2, double amount2, String description2, double incoming, double outgoing) {
        this(date2, amount2, description2);
        setIncomingInterest(incoming);
        setOutgoingInterest(outgoing);
    }

    //Copy-Konstruktor
    public Payment(Payment other){
        this(other.date,other.amount, other.description, other.incomingInterest, other.outgoingInterest);
    }

    //Funktion für die Objekte der Klasse auszudrücken.
    public void printObject(){
        if (validity) { //Überprüft Gültigkeit
            System.out.println(
                    " Date: " + getDate() + "\n" +
                            " Amount: " + getAmount() + "\n" +
                            " Description: " + getDescription() + "\n" +
                            " Incoming interest: " + getIncomingInterest() + "\n" +
                            " Outgoing interest: " + getOutgoingInterest() + "\n"
            );
        }
        else{
            System.out.println(
                    " Error, input not correct \n"
            );
        }
    }
}
