import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ParralelProcessingWithFuture {

    int fetchUser() throws InterruptedException {
        Thread.sleep(1000);
        return 10;
    }
    int fetchOrder() throws InterruptedException {
        Thread.sleep(1500);
        return 12;
    }
    int fetchRecommendation() throws InterruptedException {
        Thread.sleep(2000);
        return 21;
    }

    static void main() throws ExecutionException, InterruptedException {
        ParralelProcessingWithFuture p = new ParralelProcessingWithFuture();
        CompletableFuture<Integer> userFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return p.fetchUser();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Integer> orderFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return p.fetchOrder();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Integer> recomdFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return p.fetchRecommendation();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture.allOf(userFuture, orderFuture, recomdFuture).join();
        System.out.println(orderFuture.get());
    }
}
