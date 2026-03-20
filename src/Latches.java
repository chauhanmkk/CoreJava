import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Latches {
    private static final Semaphore dbConnections = new Semaphore(3);
    static CountDownLatch latch = new CountDownLatch(3);

    void manageConnection() throws InterruptedException {
        try {
            dbConnections.acquire();
            System.out.println("connected ");
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            dbConnections.release();
        }

    }

    void manageLatches() {
        System.out.println("latch count" + latch.getCount());
        latch.countDown();
    }

    static void main() throws InterruptedException {
        Latches l = new Latches();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.execute(l::manageLatches);
        pool.execute(l::manageLatches);
        pool.execute(l::manageLatches);
        latch.await();
        pool.shutdown();
    }
}
