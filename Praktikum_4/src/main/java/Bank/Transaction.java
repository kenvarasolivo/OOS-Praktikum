package Bank;

import Bank.Exceptions.TransactionAlreadyExistException;
import Bank.Exceptions.TransactionAttributeException;

/**
 * Die abstrakte Klasse {@code Transaction} repräsentiert eine allgemeine Transaktion mit einem Datum, einem Betrag und einer Beschreibung.
 * Sie wird von spezifischen Transaktionstypen wie {@link Payment} und {@link Transfer} erweitert.
 */
public abstract class Transaction implements CalculateBill{
    /** Das Datum der Transaktion im Format TT.MM.JJJJ. */
    protected String date;
    /** Der Betrag der Transaktion, der positiv oder negativ sein kann. */
    protected double amount;
    /** Die Beschreibung der Transaktion. */
    protected String description;

    /**
     * Gibt das Datum der Transaktion zurück.
     *
     * @return das Transaktionsdatum.
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Setzt das Datum der Transaktion.
     *
     * @param date das Datum im Format TT.MM.JJJJ.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gibt den Betrag der Transaktion zurück.
     *
     * @return der Transaktionsbetrag.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setzt den Transaktionsbetrag. Diese Methode wird von Unterklassen definiert,
     * da unterschiedliche Bedingungen für verschiedene Transaktionstypen gelten können.
     *
     * @param amount der Betrag der Transaktion.
     */
    public abstract void setAmount(double amount) throws TransactionAttributeException;

    /**
     * Gibt die Beschreibung der Transaktion zurück.
     *
     * @return die Transaktionsbeschreibung.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung der Transaktion.
     *
     * @param desc die Beschreibung der Transaktion.
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * Konstruktor für die Initialisierung der Attribute {@code date}, {@code amount} und {@code description}.
     *
     * @param date das Datum der Transaktion.
     * @param amount der Betrag der Transaktion.
     * @param description die Beschreibung der Transaktion.
     */
    public Transaction (String date , double amount, String description) throws TransactionAlreadyExistException, TransactionAttributeException {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * Gibt eine Zeichenkette zurück, die die Transaktionsdetails beschreibt.
     *
     * @return eine formatierte Zeichenkette mit den Transaktionsdetails.
     */
    @Override
    public String toString() {
        return " Date: " + getDate() + "\n" +
                " Amount: " + calculate() + "\n" +
                " Description: " + getDescription() + "\n";
    }

    /**
     * Überprüft, ob dieses {@code Transaction}-Objekt einem anderen Objekt entspricht.
     * Zwei Transaktionen werden als gleich betrachtet, wenn Datum, Betrag und Beschreibung übereinstimmen.
     *
     * @param obj das Objekt, das verglichen werden soll.
     * @return {@code true}, wenn die Objekte gleich sind, andernfalls {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Transaction other = (Transaction) obj;
        return (date.equals(other.getDate()) &&
                amount == other.getAmount() &&
                description.equals(other.getDescription()));
    }
}
