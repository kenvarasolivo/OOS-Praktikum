package Bank;

import Bank.Exceptions.TransactionAlreadyExistException;
import Bank.Exceptions.TransactionAttributeException;
import Bank.Exceptions.*;

/**
 * Die Klasse {@code Transfer} stellt eine Überweisungstransaktion zwischen einem Absender und einem Empfänger dar.
 * Sie erweitert {@link Transaction} und implementiert {@link CalculateBill}.
 */
public class Transfer extends Transaction {
    /** Der Name des Absenders der Überweisung. */
    private String sender;
    /** Der Name des Empfängers der Überweisung. */
    private String recipient; // Der Name des Empfängers

    /**
     * Gibt den Überweisungsbetrag ohne Modifikation zurück.
     *
     * @return der Überweisungsbetrag.
     */
    @Override
    public double calculate() {
        return amount;
    }

    /**
     * Setzt den Betrag der Überweisung und stellt sicher, dass dieser nicht negativ ist.
     *
     * @param amount der zu überweisende Betrag.
     */
    @Override //muss Positive sein
    public void setAmount(double amount) throws TransactionAttributeException {
        if(amount >= 0) {
            this.amount = amount;
        } else {
            throw new TransactionAttributeException (" Amount can't be negative ");
        }
    }

    /**
     * Gibt den Namen des Absenders zurück.
     *
     * @return der Name des Absenders.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Setzt den Namen des Absenders.
     *
     * @param sender der Name des Absenders.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Gibt den Namen des Empfängers zurück.
     *
     * @return der Name des Empfängers.
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Setzt den Namen des Empfängers.
     *
     * @param recipient der Name des Empfängers.
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Konstruktor für ein {@code Transfer}-Objekt mit Datum, Betrag und Beschreibung.
     *
     * @param date das Datum der Überweisung.
     * @param amount der Überweisungsbetrag.
     * @param description die Beschreibung der Überweisung.
     */
    public Transfer(String date, double amount, String description) throws TransactionAttributeException, TransactionAlreadyExistException {
        super(date, amount, description);
    }

    /**
     * Konstruktor für ein {@code Transfer}-Objekt mit allen Attributen.
     *
     * @param date2 das Datum der Überweisung.
     * @param amount2 der Überweisungsbetrag.
     * @param description2 die Beschreibung der Überweisung.
     * @param sender der Name des Absenders.
     * @param recipient der Name des Empfängers.
     */
    public Transfer(String date2, double amount2, String description2, String sender, String recipient) throws TransactionAlreadyExistException, TransactionAttributeException {
        super(date2,amount2,description2);
        setSender(sender);
        setRecipient(recipient);
    }

    /**
     * Kopierkonstruktor für die Erstellung eines {@code Transfer}-Objekts auf Basis eines anderen {@code Transfer}.
     *
     * @param other das zu kopierende {@code Transfer}-Objekt.
     */
    public Transfer(Transfer other) throws TransactionAlreadyExistException, TransactionAttributeException {
        this(other.date, other.amount, other.description, other.sender, other.recipient);
    }

    /**
     * Gibt eine Zeichenkette zurück, die das {@code Transfer} beschreibt.
     *
     * @return eine formatierte Zeichenkette mit den Überweisungsdetails.
     */
    @Override
    public String toString() {
        return super.toString() + " Sender: " + getSender() + "\n" +
                " Recipient: " + getRecipient() + "\n";
    }

    /**
     * Überprüft, ob dieses {@code Transfer}-Objekt einem anderen Objekt entspricht.
     *
     * @param obj das Objekt, das verglichen werden soll.
     * @return {@code true}, wenn die Objekte gleich sind, andernfalls {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;

        Transfer that = (Transfer) obj;
        return sender.equals(that.sender) && recipient.equals(that.recipient);
    }
}
