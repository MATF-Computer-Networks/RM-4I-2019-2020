package p03_bank_synced_locks;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedBank {

    private final int[] accounts;

    // Java's way of doing locks - Lock is an interface which we will use
    private Lock lock;
    // Each lock can provide a special condition which can be listened from other threads
    private Condition insufficientFunds;


    public LockedBank(int accountsNum, int initialBalance) {
        this.accounts = new int[accountsNum];
        Arrays.fill(this.accounts, initialBalance);

        // Since Lock is an interface, we cannot instantiate it
        // We can, however, instantiate some classes that implement it, such as ReentrantLock
        // (or create our own but why do it when it is already done?)
        this.lock = new ReentrantLock();

        // We also create a condition on the lock - for cases where there aren't enough funds
        // on the account to perform the transfer
        this.insufficientFunds = this.lock.newCondition();
    }


    // The bank provides an option to transfer the funds between accounts
    // This function will be called from different threads, so there will be race condition
    public void transfer(int from, int to, int amount) throws InterruptedException {
        // Remember this try-finally pattern! We must make sure to unlock in case of exception
        this.lock.lock();
        try {
            while (this.accounts[from] < amount) {
                // If there are no funds on the account, the thread is blocked here
                // and waiting for a signal that a transfer is made - then we re-check
                // if there is enough funds on the account
                this.insufficientFunds.await();

                // await() method REQUIRES the lock to be held during the call, it will
                // atomically unlock and wait on the condition (that is why it is safe
                // to wait here - we will not be holding locks and block other threads)
            }

            System.out.println(Thread.currentThread());
            this.accounts[from] -= amount;
            this.accounts[to] += amount;
            System.out.printf("Transfer from %3d to %3d: %5d\n", from, to, amount);
            System.out.println("Total balance: " + this.getTotalBalance());

            // We are signalling to all threads that are listening on our lock condition
            // that a transfer has been made, and that they should check the funds again
            this.insufficientFunds.signalAll();
        } finally {
            this.lock.unlock();
        }
    }


    public int getTotalBalance() {
        return Arrays.stream(accounts).sum();
    }

    public int count() {
        return accounts.length;
    }
}
