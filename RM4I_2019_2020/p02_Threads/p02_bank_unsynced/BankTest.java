package p02_bank_unsynced;

public class BankTest {
    private static final int ACCOUNTS = 5;
    private static final int STARTING_BALANCE = 1000000;


    public static void main(String[] args) {
        Bank bank = new Bank(ACCOUNTS, STARTING_BALANCE);

        for (int i = 0; i < ACCOUNTS; i++) {
            TransferRunnable transfer = new TransferRunnable(bank, i, 10);
            Thread t = new Thread(transfer);
            t.start();
        }

        // Why don't we wait for all threads to finish? Why doesn't the program terminate?
    }

}
