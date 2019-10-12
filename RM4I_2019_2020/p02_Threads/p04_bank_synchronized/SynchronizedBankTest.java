package p04_bank_synchronized;

public class SynchronizedBankTest {
    private static final int ACCOUNTS = 5;
    private static final int STARTING_BALANCE = 1000000;


    public static void main(String[] args) {
        SynchronizedBank bank = new SynchronizedBank(ACCOUNTS, STARTING_BALANCE);

        for (int i = 0; i < ACCOUNTS; i++) {
            TransferRunnable transfer = new TransferRunnable(bank, i, 10);
            Thread t = new Thread(transfer);
            t.start();
        }
    }

}
