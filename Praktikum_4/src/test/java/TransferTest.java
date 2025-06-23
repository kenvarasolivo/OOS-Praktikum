import Bank.Exceptions.TransactionAlreadyExistException;
import Bank.Exceptions.TransactionAttributeException;
import Bank.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    private Transfer transfer;

    @BeforeEach
    void setUp() throws TransactionAttributeException, TransactionAlreadyExistException {
        // Initialize a valid Transfer object for use in most tests
        transfer = new Transfer("01.12.2024", 500.0, "Rent Payment", "Alice", "Bob");
    }

    /**
     * Tests the constructor for proper initialization.
     */
    @Test
    void testConstructor() throws TransactionAttributeException, TransactionAlreadyExistException {
        Transfer transfer = new Transfer("10.12.2024", 1000.0, "Loan Payment", "Charlie", "Diana");
        assertEquals("10.12.2024", transfer.getDate());
        assertEquals(1000.0, transfer.calculate());
        assertEquals("Loan Payment", transfer.getDescription());
        assertEquals("Charlie", transfer.getSender());
        assertEquals("Diana", transfer.getRecipient());
    }

    @Test
    void testConstructor2() throws TransactionAttributeException, TransactionAlreadyExistException {
        Transfer transfer = new Transfer("10.12.2024", 1000.0, "Loan Payment");
        assertEquals("10.12.2024", transfer.getDate());
        assertEquals(1000.0, transfer.calculate());
        assertEquals("Loan Payment", transfer.getDescription());
    }

    /**
     * Tests the copy constructor for creating an identical Transfer object.
     */
    @Test
    void testCopyConstructor() throws TransactionAttributeException, TransactionAlreadyExistException {
        Transfer copy = new Transfer(transfer);
        assertEquals(transfer, copy, "Copy constructor did not produce an identical object.");
    }

    /**
     * Tests the calculate method.
     */
    @Test
    void testCalculate() {
        assertEquals(500.0, transfer.calculate(), "Calculate method should return the same amount for transfers.");
    }

    /**
     * Tests the setAmount method with valid and invalid inputs.
     */
    @Test
    void testSetAmount() {
        assertDoesNotThrow(() -> transfer.setAmount(1000.0), "Setting a valid amount should not throw an exception.");
        assertEquals(1000.0, transfer.calculate());

        Exception exception = assertThrows(TransactionAttributeException.class, () -> transfer.setAmount(-500.0));
        assertEquals(" Amount can't be negative ", exception.getMessage());
    }

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        String expected = " Date: 01.12.2024\n" +
                " Amount: 500.0\n" +
                " Description: Rent Payment\n" +
                " Sender: Alice\n" +
                " Recipient: Bob\n";
        assertEquals(expected, transfer.toString(), "toString output is incorrect.");
    }

    /**
     * Tests the equals method for correctness.
     */
    @Test
    void testEquals() throws TransactionAttributeException, TransactionAlreadyExistException {
        Transfer identicalTransfer = new Transfer("01.12.2024", 500.0, "Rent Payment", "Alice", "Bob");
        Transfer differentTransfer = new Transfer("02.12.2024", 300.0, "Utility Payment", "Alice", "Eve");

        assertEquals(transfer, identicalTransfer, "equals method should return true for identical transfers.");
        assertNotEquals(transfer, differentTransfer, "equals method should return false for different transfers.");
    }
}

