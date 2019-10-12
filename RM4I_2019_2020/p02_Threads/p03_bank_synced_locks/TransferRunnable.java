package p03_bank_synced_locks;

public class TransferRunnable implements Runnable {
    private static final int MAX_DELAY = 1;

    private LockedBank bank;
    private int from;
    private int max;


    public TransferRunnable(LockedBank bank, int from, int max) {
        this.bank = bank;
        this.from = from;
        this.max = max;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Transfer funds to a random account
                int to = (int)(Math.random() * this.bank.count());
                this.bank.transfer(this.from, to, (int)(Math.random() * this.max));
                Thread.sleep((int)(Math.random() * MAX_DELAY));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
