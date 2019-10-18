package p02_bank;

import java.util.concurrent.ThreadLocalRandom;

public class TransferRunnable implements Runnable {
    // We do not want to clog other threads, right?
    private static final int MAX_TRANSFER_DELAY = 2;

    private IBank bank;
    private int from;
    private int max;


    public TransferRunnable(IBank bank, int from, int max) {
        this.bank = bank;
        this.from = from;
        this.max = max;
    }

    @Override
    public void run() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                // Transfer funds to a random account
                int to = r.nextInt(this.bank.count());
                this.bank.transfer(this.from, to, r.nextInt(this.max));
                Thread.sleep(r.nextLong(MAX_TRANSFER_DELAY));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
