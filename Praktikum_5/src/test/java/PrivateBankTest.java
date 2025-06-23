import Bank.*;
import Bank.Exceptions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class PrivateBankTest {
    PrivateBank bank1;
    PrivateBank bank2;

    @BeforeEach
    void init() throws IOException, TransactionAttributeException {
        // Cleanup before tests
        File directory = new File("./bankData");
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }

        bank1 = new PrivateBank("bank1", 0.2, 0.1);
        bank2 = new PrivateBank("bank2", 0.5, 0.75);
    }

    @AfterEach
    void delete() throws IOException {

        // Cleanup after each test
        File directory = new File("./bankData");
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.getName().equalsIgnoreCase("Ken.json") && !file.getName().equalsIgnoreCase("Bob.json")) {
                        file.delete();
                    }
                }
            }
        }
    }

    @Test
    void testConstructor() {
        assertEquals("bank1", bank1.getName());
        assertEquals(0.2, bank1.getIncomingInterest());
        assertEquals(0.1, bank1.getOutgoingInterest());

        assertEquals("bank2", bank2.getName());
        assertEquals(0.5, bank2.getIncomingInterest());
        assertEquals(0.75, bank2.getOutgoingInterest());

        String bank3 = "bank3";
        bank1.setName(bank3);
        assertEquals("bank3", bank1.getName());
        assertEquals(0.2, bank1.getIncomingInterest());
        assertEquals(0.1, bank1.getOutgoingInterest());
    }

    @Test
    void testCopyConstructor() throws IOException, TransactionAttributeException {
        PrivateBank copy = new PrivateBank(bank1);
        assertEquals(bank1, copy);
        assertNotEquals(bank2, copy);
    }

    @Test
    void testToString() {
        String string1 = "Name :bank1\n" +
                "Incoming Interest: 0.2\n" +
                "Outgoing Interest: 0.1";
        assertEquals(string1, bank1.toString());

        String string2 = "Name :bank2\n" +
                "Incoming Interest: 0.5\n" +
                "Outgoing Interest: 0.75";
        assertEquals(string2, bank2.toString());
    }

    @Test
    void testEquals() throws IOException, TransactionAttributeException {
        PrivateBank copy = new PrivateBank(bank1);
        assertTrue(bank1.equals(copy));
        assertFalse(bank1.equals(bank2));
    }

    @Test
    void testCreateAccount1() throws TransactionAlreadyExistException, AccountAlreadyExistsException, IOException, AccountDoesNotExistException, TransactionAttributeException {
        bank1.createAccount("vara");

        assertTrue(bank1.accountsToTransactions.containsKey("vara"));
        assertTrue(bank1.accountsToTransactions.get("vara").isEmpty());

        File accountFile = new File("bankData/vara.json");
        assertTrue(accountFile.exists());

        assertThrows(AccountAlreadyExistsException.class, () -> {
            bank1.createAccount("vara");
        });
    }

    @Test
    void testCreateAccount2() throws TransactionAlreadyExistException, AccountAlreadyExistsException, IOException, AccountDoesNotExistException, TransactionAttributeException {
        bank1.createAccount("solivo", new ArrayList<>(List.of(new Payment("04.12.2024", 100, "SPOTIFY"))));

        assertTrue(bank1.accountsToTransactions.containsKey("solivo"));

        File accountFile = new File("bankData/solivo.json");
        assertTrue(accountFile.exists());

        assertThrows(AccountAlreadyExistsException.class, () -> {
            bank1.createAccount("solivo");
        });
    }

    @Test
    void addTransaction() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        bank1.createAccount("solivo");
        Payment payment1 = new Payment("04.12.2024", 100, "youtube");

        // Add transaction and check if it's added successfully
        bank1.addTransaction("solivo", payment1);
        assertTrue(bank1.accountsToTransactions.containsKey("solivo"));
        assertTrue(bank1.accountsToTransactions.get("solivo").contains(payment1));
        assertEquals(1, bank1.accountsToTransactions.get("solivo").size());

        // Verify file creation after adding the transaction
        File accountFile = new File("bankData/solivo.json");
        assertTrue(accountFile.exists());

        // Add another transaction and verify
        Payment payment2 = new Payment("05.12.2024", 50, "netflix");
        bank1.addTransaction("solivo", payment2);
        assertEquals(2, bank1.accountsToTransactions.get("solivo").size());

        // Attempt adding an invalid transaction and check exceptions
        assertThrows(TransactionAttributeException.class, () -> {
            Transfer transfer1 = new Transfer("06.12.2024", -85, "spotify");
            bank1.addTransaction("solivo", transfer1);
        });

        // Attempt adding a duplicate transaction and check exceptions
        assertThrows(TransactionAlreadyExistException.class, () -> {
            bank1.addTransaction("solivo", payment1);
        });
    }

    @Test
    void containsTransaction() throws TransactionAlreadyExistException, AccountAlreadyExistsException, IOException, AccountDoesNotExistException, TransactionAttributeException {
        IncomingTransfer transfer1 = new IncomingTransfer("01.12.2024", 50, "Überweisung", "solivo", "vara");

        bank1.createAccount("vara");
        bank1.addTransaction("vara", transfer1);

        List<Transaction> transactions = bank1.getTransactions("vara");
        assertEquals(1, transactions.size());
        assertTrue(transactions.contains(transfer1));

        assertTrue(bank1.containsTransaction("vara", transfer1));
        assertFalse(bank1.containsTransaction("vara", new IncomingTransfer("11.12.2024", 20, "Überweisung", "vara", "solivo")));
    }

    @Test
    void removeTransaction() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, TransactionDoesNotExistException {
        IncomingTransfer transfer1 = new IncomingTransfer("01.12.2024", 50, "Überweisung", "solivo", "vara");
        OutgoingTransfer transfer2 = new OutgoingTransfer("02.12.2024", 75, "Überweisung", "vara", "solivo");

        bank1.createAccount("vara");
        bank1.addTransaction("vara", transfer1);
        bank1.addTransaction("vara", transfer2);

        // Remove transaction and validate
        bank1.removeTransaction("vara", transfer1);
        List<Transaction> transactions = bank1.getTransactions("vara");
        assertEquals(1, transactions.size());
        assertTrue(transactions.contains(transfer2));

        // Attempt removing a non-existent transaction
        assertThrows(TransactionDoesNotExistException.class, () -> {
            bank1.removeTransaction("vara", transfer1);
        });

        // Attempt removing a transaction from a non-existent account
        assertThrows(AccountDoesNotExistException.class, () -> {
            bank1.removeTransaction("solivo", transfer1);
        });
    }

    @Test
    void testGetAccountBalance() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException {
        bank1.createAccount("testAccount", new ArrayList<>(List.of(
                new Payment("01.12.2024", 200, "Salary"),
                new OutgoingTransfer("02.12.2024", 50, "Rent", "testAccount", "Landlord")
        )));

        // Verify account balance calculation
        double balance = bank1.getAccountBalance("testAccount");
        assertEquals(150, balance); // 200 - 50 = 150
    }

    @Test
    void testGetTransactionsSorted() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException {
        bank1.createAccount("sortedAccount", new ArrayList<>(List.of(
                new Payment("01.12.2024", 200, "Salary"),
                new OutgoingTransfer("02.12.2024", 50, "Rent", "sortedAccount", "Landlord"),
                new Payment("03.12.2024", 100, "Bonus")
        )));

        // Ascending order
        List<Transaction> sortedAsc = bank1.getTransactionsSorted("sortedAccount", true);
        assertEquals(3, sortedAsc.size());
        assertEquals(-50, sortedAsc.get(0).calculate()); // OutgoingTransfer
        assertEquals(100, sortedAsc.get(1).calculate()); // Payment
        assertEquals(200, sortedAsc.get(2).calculate()); // Payment

        // Descending order
        List<Transaction> sortedDesc = bank1.getTransactionsSorted("sortedAccount", false);
        assertEquals(3, sortedDesc.size());
        assertEquals(200, sortedDesc.get(0).calculate()); // Payment
        assertEquals(100, sortedDesc.get(1).calculate()); // Payment
        assertEquals(-50, sortedDesc.get(2).calculate()); // OutgoingTransfer
    }

    @Test
    void testGetTransactionsByType() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException {
        bank1.createAccount("typeAccount", new ArrayList<>(List.of(
                new Payment("01.12.2024", 200, "Salary"),
                new OutgoingTransfer("02.12.2024", 50, "Rent", "typeAccount", "Landlord"),
                new Payment("03.12.2024", 100, "Bonus")
        )));

        // Positive transactions
        List<Transaction> positiveTransactions = bank1.getTransactionsByType("typeAccount", true);
        assertEquals(2, positiveTransactions.size());
        assertTrue(positiveTransactions.stream().allMatch(t -> t.calculate() >= 0));

        // Negative transactions
        List<Transaction> negativeTransactions = bank1.getTransactionsByType("typeAccount", false);
        assertEquals(1, negativeTransactions.size());
        assertTrue(negativeTransactions.stream().allMatch(t -> t.calculate() < 0));
    }

    @Test
    void testWriteAccount() throws IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        // Create an account and add transactions
        bank1.createAccount("testAccount");
        Payment payment1 = new Payment("12.12.2024", 500, "Salary");
        Payment payment2 = new Payment("13.12.2024", -200, "Rent");
        bank1.addTransaction("testAccount", payment1);
        bank1.addTransaction("testAccount", payment2);

        // Write account data to JSON
        bank1.writeAccount("testAccount");

        // Verify JSON file exists and contains correct data
        File accountFile = new File("./bankData/testAccount.json");
        assertTrue(accountFile.exists());

        // Read back the JSON content
        String jsonContent = new String(Files.readAllBytes(accountFile.toPath()));
        assertTrue(jsonContent.contains("Salary"));
        assertTrue(jsonContent.contains("Rent"));
        assertTrue(jsonContent.contains("500"));
        assertTrue(jsonContent.contains("-200"));
    }

    @Test
    void testReadAccounts() throws IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        // Write test data to JSON files manually
        String jsonContent = """
    [
        {
            "CLASSNAME": "Bank.OutgoingTransfer",
            "INSTANCE": {
                "sender": "Ken",
                "recipient": "Hanif",
                "date": "09.11.2024",
                "amount": 520.0,
                "description": "this is Transfer"
            }
        },
        {
            "CLASSNAME": "Bank.Payment",
            "INSTANCE": {
                "incomingInterest": 0.15,
                "outgoingInterest": 0.1,
                "date": "10.11.2024",
                "amount": 1200.0,
                "description": "this is deposit"
            }
        },
        {
            "CLASSNAME": "Bank.Payment",
            "INSTANCE": {
                "incomingInterest": 0.12,
                "outgoingInterest": 0.3,
                "date": "11.11.2024",
                "amount": -1100.0,
                "description": "this is deposit"
            }
        },
        {
            "CLASSNAME": "Bank.IncomingTransfer",
            "INSTANCE": {
                "sender": "Timothy",
                "recipient": "Ken",
                "date": "12.11.2024",
                "amount": 450.0,
                "description": "this is Transfer"
            }
        }
    ]
    """;

        File dir = new File("./bankData");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File accountFile = new File("./bankData/testAccount.json");
        try (FileWriter writer = new FileWriter(accountFile)) {
            writer.write(jsonContent);
        }

        // Call the readAccounts method
        bank1.readAccounts();

        // Verify the account is populated correctly
        assertTrue(bank1.accountsToTransactions.containsKey("testAccount"));
        List<Transaction> transactions = bank1.accountsToTransactions.get("testAccount");
        assertEquals(4, transactions.size());
        assertEquals(-520.0, transactions.get(0).calculate());
        assertEquals(1020.0, transactions.get(1).calculate());
        assertEquals(-1430.0, transactions.get(2).calculate());
        assertEquals(450.0, transactions.get(3).calculate());
        assertEquals("this is Transfer", transactions.get(0).getDescription());
        assertEquals("this is deposit", transactions.get(1).getDescription());
        assertEquals("this is deposit", transactions.get(2).getDescription());
        assertEquals("this is Transfer", transactions.get(3).getDescription());
    }
}