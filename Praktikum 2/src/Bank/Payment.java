package Bank;

/**
 * Die Klasse {@code Payment} stellt eine Transaktion dar, die Einzahlungen und Abhebungen mit Zinsen beinhaltet.
 * Sie erweitert {@link Transaction} und wendet Zinsen für Ein- und Auszahlungen an, abhängig von der Art der Transaktion.
 */
public class Payment extends Transaction {
    /** Zinssatz für Einzahlungen, Wert muss zwischen 0 und 1 liegen. */
    private double incomingInterest;
    /** Zinssatz für Abhebungen, Wert muss zwischen 0 und 1 liegen. */
    private double outgoingInterest; //Zinsen bei Withdrawal, Wert muss von 0 bis 1 sein

    /**
     * Berechnet den effektiven Betrag nach Anwendung der Ein- oder Auszahlungszinsen.
     * Wenn der Betrag positiv ist, wird der Zinssatz für Einzahlungen angewendet; andernfalls der Zinssatz für Abhebungen.
     *
     * @return der berechnete effektive Betrag.
     */
    @Override
    public double calculate() {
        if (amount > 0) {
            return amount * (1 - incomingInterest);
        } else {
            return amount * (1 + outgoingInterest);
        }
    }

    /**
     * Setzt den Betrag der Transaktion. Dieser Betrag kann positiv oder negativ sein.
     *
     * @param amount der Transaktionsbetrag.
     */
    @Override //kann Negative sein
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gibt den Zinssatz für Einzahlungen zurück.
     *
     * @return der Zinssatz für Einzahlungen.
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Setzt den Zinssatz für Einzahlungen und prüft, dass dieser zwischen 0 und 1 liegt.
     *
     * @param IncomingInterest der Zinssatz für Einzahlungen.
     */
    public void setIncomingInterest(double IncomingInterest) {
        if (IncomingInterest >= 0 && IncomingInterest <= 1) { //Überprüft wenn es zwischen 0 und 1
            this.incomingInterest = IncomingInterest;
        } else {
            System.out.println(" Not a valid Incoming Interest ");

        }
    }

    /**
     * Gibt den Zinssatz für Abhebungen zurück.
     *
     * @return der Zinssatz für Abhebungen.
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Setzt den Zinssatz für Abhebungen und prüft, dass dieser zwischen 0 und 1 liegt.
     *
     * @param OutgoingInterest der Zinssatz für Abhebungen.
     */
    public void setOutgoingInterest(double OutgoingInterest) {
        if (OutgoingInterest >= 0 && OutgoingInterest <= 1) { //Überprüft wenn es zwischen 0 und 1
            this.outgoingInterest = OutgoingInterest;
        } else {
            System.out.println(" Not a valid Outgoing Interest ");
        }
    }


    /**
     * Konstruktor für ein {@code Payment} mit Datum, Betrag und Beschreibung.
     *
     * @param date das Datum der Transaktion im Format TT.MM.JJJJ.
     * @param amount der Transaktionsbetrag.
     * @param description die Beschreibung der Transaktion.
     */
    public Payment(String date, double amount, String description) {
        super(date, amount, description);
    }

    /**
     * Konstruktor für ein {@code Payment} mit allen Attributen.
     *
     * @param date2 das Datum der Transaktion.
     * @param amount2 der Transaktionsbetrag.
     * @param description2 die Beschreibung der Transaktion.
     * @param incoming der Zinssatz für Einzahlungen.
     * @param outgoing der Zinssatz für Abhebungen.
     */
    public Payment(String date2, double amount2, String description2, double incoming, double outgoing) {
        super(date2, amount2, description2);
        setIncomingInterest(incoming);
        setOutgoingInterest(outgoing);
    }

    /**
     * Kopierkonstruktor, der ein neues {@code Payment} mit denselben Eigenschaften wie ein anderes {@code Payment} erstellt.
     *
     * @param other das {@code Payment}-Objekt, das kopiert werden soll.
     */
    public Payment(Payment other) {
        this(other.date, other.amount, other.description, other.incomingInterest, other.outgoingInterest);
    }

    /**
     * Gibt eine Zeichenkette zurück, die das {@code Payment} beschreibt.
     *
     * @return eine formatierte Zeichenkette mit den Zahlungsdetails.
     */
    @Override
    public String toString() {
        return super.toString() + " Incoming interest: " + getIncomingInterest() + "\n" +
                            " Outgoing interest: " + getOutgoingInterest();
    }

    /**
     * Überprüft, ob dieses {@code Payment} einem anderen Objekt entspricht.
     *
     * @param obj das Objekt, das verglichen werden soll.
     * @return {@code true}, wenn die Objekte gleich sind, andernfalls {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;

        Payment that = (Payment) obj;
        return Double.compare(that.incomingInterest, incomingInterest) == 0 &&
                Double.compare(that.outgoingInterest, outgoingInterest) == 0;
    }
}