import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Scenario: You have a method transfer(Account from, Account to, int amount) that locks both accounts before transferring.
 * Two threads run simultaneously:
 *
 * Thread 1: transfer(accountA, accountB, 100)
 * Thread 2: transfer(accountB, accountA, 200)
 *
 * Questions:
 *
 * What problem can occur here? Explain why.
 * Fix it using lock ordering
 * Fix it using tryLock with timeout (ReentrantLock)
 */

public class Transfer {
    static class Account {
        int accountNum;
        int balance;
        ReentrantLock lock = new ReentrantLock();
        Account(int accountNum) {
            this.accountNum = accountNum;
            this.balance = 0;
        }
    }
    void transfer(Account from, Account to, int amount) throws InterruptedException {
        boolean fromLocked = false;
        boolean toLocked   = false;
        try {
            fromLocked = from.lock.tryLock(1, TimeUnit.SECONDS);
            toLocked   =   to.lock.tryLock(1, TimeUnit.SECONDS);

            if (fromLocked && toLocked) {
                from.balance -= amount;
                to.balance   += amount;
                return;
            }
        } finally {
            if (fromLocked) from.lock.unlock();
            if (toLocked)     to.lock.unlock();
        }
    }
    synchronized void syncTransfer(Account from, Account to, int amount) {
        from.balance -= amount;
        to.balance += amount;
    }

    static void main() {
        Account a1 = new Account(123);
        Account a2 = new Account(456);
        Transfer transfer = new Transfer();
        Thread t1 = new Thread(() -> {
            try {
                transfer.transfer(a1,a2,100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                transfer.transfer(a1,a2,100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
