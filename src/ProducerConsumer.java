import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumer {
    Queue<Integer> queue = new ArrayDeque<>();

    synchronized void produce(int num) throws InterruptedException {
        while (queue.size() >=5) {
            wait();
        }
        queue.add(num);
        System.out.println("Produced: " + num + " | Queue size: " + queue.size());
        notifyAll();
    }

    synchronized void consume() throws InterruptedException {
        while (queue.isEmpty()) wait();
        int poll = queue.poll();
        System.out.println("Consumed: " + poll + " | Queue size: " + queue.size());
        notifyAll();
    }

    public static void main() {
        ProducerConsumer p = new ProducerConsumer();
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        // Submit tasks for execution
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                try {
                    p.produce(finalI);
//                    p.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            threadPool.execute(() -> {
                try {
                    p.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        threadPool.shutdown();
    }
}
