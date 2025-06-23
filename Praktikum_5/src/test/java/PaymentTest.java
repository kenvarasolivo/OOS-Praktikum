import Bank.Exceptions.TransactionAttributeException;
import Bank.Exceptions.TransactionAlreadyExistException;
import Bank.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Payment class.
 */
public class PaymentTest {

    private Payment payment;

    /**
     * Sets up a basic payment object before each test.
     */
    @BeforeEach
    void setUp() throws TransactionAttributeException, TransactionAlreadyExistException {
        // Initialize a valid Payment object for use in most tests
        payment = new Payment("01.12.2024", 1000.0, "Salary", 0.05, 0.10);
    }

    /**
     * Tests the constructor with valid values.
     */
    @Test
    void testConstructor() throws TransactionAttributeException, TransactionAlreadyExistException {
        Payment payment = new Payment("01.12.2024", 500.0, "Bonus", 0.03, 0.07);
        assertEquals("01.12.2024", payment.getDate());
        assertEquals(485.0, payment.calculate());
        assertEquals("Bonus", payment.getDescription());
        assertEquals(0.03, payment.getIncomingInterest());
        assertEquals(0.07, payment.getOutgoingInterest());
    }

    @Test
    void testConstructor2() throws TransactionAttributeException, TransactionAlreadyExistException {
        Payment payment = new Payment("01.12.2024", 500.0, "Bonus");
        assertEquals("01.12.2024", payment.getDate());
        assertEquals(500.0, payment.calculate());
        assertEquals("Bonus", payment.getDescription());
    }

    /**
     * Tests the constructor with invalid interest rates.
     */
    @ParameterizedTest
    @CsvSource({
            "-0.1, 0.05",
            "0.1, -0.05",
            "1.1, 0.05",
            "0.1, 1.1"
    })
    void testConstructorInvalidInterest(double incoming, double outgoing) {
        assertThrows(TransactionAttributeException.class,
                () -> new Payment("01.12.2024", 500.0, "Bonus", incoming, outgoing));
    }

    /**
     * Tests the copy constructor.
     */
    @Test
    void testCopyConstructor() {
        try {
            Payment copiedPayment = new Payment(payment);
            assertEquals(payment, copiedPayment);
            assertNotSame(payment, copiedPayment, "Copy constructor should create a new object.");
        } catch (TransactionAlreadyExistException | TransactionAttributeException e) {
            fail("Copy constructor test failed due to unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Tests the calculation of the effective amount.
     */
    @ParameterizedTest
    @CsvSource({
            "1000, 950.0",   // Positive amount with incoming interest
            "-1000, -1100.0" // Negative amount with outgoing interest
    })
    void testCalculate(double amount, double expected) {
        payment.setAmount(amount);
        assertEquals(expected, payment.calculate(), 0.01, "Calculation result is incorrect.");
    }

    /**
     * Tests the incoming interest setter with valid values.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.5, 1.0})
    void testSetIncomingInterestValid(double validInterest) {
        assertDoesNotThrow(() -> payment.setIncomingInterest(validInterest));
    }

    /**
     * Tests the incoming interest setter with invalid values.
     */
    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 1.1})
    void testSetIncomingInterestInvalid(double invalidInterest) {
        assertThrows(TransactionAttributeException.class,
                () -> payment.setIncomingInterest(invalidInterest));
    }

    /**
     * Tests the outgoing interest setter with valid values.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.5, 1.0})
    void testSetOutgoingInterestValid(double validInterest) {
        assertDoesNotThrow(() -> payment.setOutgoingInterest(validInterest));
    }

    /**
     * Tests the outgoing interest setter with invalid values.
     */
    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 1.1})
    void testSetOutgoingInterestInvalid(double invalidInterest) {
        assertThrows(TransactionAttributeException.class,
                () -> payment.setOutgoingInterest(invalidInterest));
    }

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        String expected = " Date: 01.12.2024\n" +
                " Amount: 950.0\n" +
                " Description: Salary\n" +
                " Incoming interest: 0.05\n" +
                " Outgoing interest: 0.1\n";
        assertEquals(expected, payment.toString(), "toString output is incorrect.");
    }

    /**
     * Tests the equals method for identical and different objects.
     */
    @Test
    void testEquals() {
        try {
            Payment identicalPayment = new Payment("01.12.2024", 1000.0, "Salary", 0.05, 0.10);
            Payment differentPayment = new Payment("01.12.2024", 500.0, "Bonus", 0.05, 0.10);

            assertEquals(payment, identicalPayment, "Payments with identical attributes should be equal.");
            assertNotEquals(payment, differentPayment, "Payments with different attributes should not be equal.");
        } catch (TransactionAlreadyExistException | TransactionAttributeException e) {
            fail("Equals test failed due to unexpected exception: " + e.getMessage());
        }
    }
}
