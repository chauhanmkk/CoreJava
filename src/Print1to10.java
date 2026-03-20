import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Print1to10 {
    ReentrantLock lock = new ReentrantLock();
    Condition oddTurn  = lock.newCondition();
    Condition evenTurn = lock.newCondition();
    boolean isOddTurn  = true; // ← shared turn state

    void printOdd() throws InterruptedException {
        lock.lock();
        try {
            for (int i = 1; i <= 10; i += 2) {
                while (!isOddTurn) oddTurn.await();  // wait if not my turn
                System.out.println("Odd:  " + i);
                isOddTurn = false;                   // hand turn to even
                evenTurn.signal();                   // wake even thread
            }
        } finally {
            lock.unlock();
        }
    }

    void printEven() throws InterruptedException {
        lock.lock();
        try {
            for (int i = 2; i <= 10; i += 2) {
                while (isOddTurn) evenTurn.await();  // wait if not my turn
                System.out.println("Even: " + i);
                isOddTurn = true;                    // hand turn to odd
                oddTurn.signal();                    // wake odd thread
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Print1to10 p = new Print1to10();

        Thread t1 = new Thread(() -> {
            try { p.printOdd(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        });
        Thread t2 = new Thread(() -> {
            try { p.printEven(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        });

        t1.start();
        t2.start();
    }
}
