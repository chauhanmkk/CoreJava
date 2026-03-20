import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * You have a shared counter being incremented by 100 threads simultaneously.
 * Write it using synchronized, then rewrite using ReentrantLock.
 * Also answer: What's one thing ReentrantLock can do that synchronized cannot?
 */
public class SharedCounter {

    // ✅ Shared state — must be instance variable
    private int count = 0;

    // ✅ Lock must be shared — instance variable
    private final ReentrantLock lock = new ReentrantLock();

    // ✅ AtomicInteger approach
    private AtomicInteger atomicCount = new AtomicInteger(0);

    void increaseUsingAtomic() {
        atomicCount.incrementAndGet();
    }

    // ✅ synchronized on shared instance variable
    synchronized void increaseUsingSync() {
        count++;
    }

    // ✅ ReentrantLock on shared lock, unlock ALWAYS in finally
    void incrementUsingReentrant() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock(); // always releases even if exception
        }
    }
}