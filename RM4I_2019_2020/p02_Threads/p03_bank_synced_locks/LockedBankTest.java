package p03_bank_synced_locks;

public class LockedBankTest {
    private static final int ACCOUNTS = 5;
    private static final int STARTING_BALANCE = 1000000;


    public static void main(String[] args) {
        LockedBank bank = new LockedBank(ACCOUNTS, STARTING_BALANCE);

        for (int i = 0; i < ACCOUNTS; i++) {
            TransferRunnable transfer = new TransferRunnable(bank, i, 10);
            Thread t = new Thread(transfer);
            t.start();
        }
    }

}
