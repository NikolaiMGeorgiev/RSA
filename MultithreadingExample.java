import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultithreadingExample {

    public static final int WIDTH = 3840;
    public static final int HEIGHT = 2160;
    private static final int ALL_PIXELS = WIDTH * HEIGHT;

    /*
    private static final double STARTX = -1.5;
    private static final double ENDX = 0.5;
    private static final double STARTY = -1;
    */

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Instant start = Instant.now();
        final int THREADS = Integer.parseInt(args[0]);
        final int G = Integer.parseInt(args[1]);
        int sectorWidth = WIDTH / THREADS;
        int sectorHeight = HEIGHT / G;

        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

        List<Future<List<Integer>>> futures = new ArrayList<>();

        for (int thread = 0; thread < THREADS; thread++) {
            int startY = thread * sectorHeight;
            int startX = thread * sectorWidth;
            futures.add(executorService.submit(new Mandelbrot(startX, startY, sectorWidth, sectorHeight, G, THREADS)));
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        List<List<Integer>> allMandelbrots = new ArrayList<>();
        for (Future<List<Integer>> future : futures) {
            allMandelbrots.add(future.get());
        }
        drawMandelbrots(allMandelbrots, sectorWidth, sectorHeight, THREADS, G);
        System.out.println("Took " + start.until(Instant.now(), ChronoUnit.MILLIS) + "ms");
    }

    private static void drawMandelbrots(List<List<Integer>> allMandelbrots, int lengthX, int lengthY, int threads, int g) {
        /*
        for (List<Integer> pixels : allMandelbrots) {
            for (int y = 0; y < pixels.size(); y++) {
                StringBuilder output = new StringBuilder();
                int xs = pixels.get(y);
                for (int x = 0; x < xs.size(); x++) {
                    output.append(xs.get(x) + " ");
                }
                System.out.println(output.toString());
            }
        }*/
        int currentThread = 0;
        int iterations = 0;
        int index = 0;
        for (int bx = 0; bx < threads; bx++) {
            for (int by = 0; by < g; by++) {

                StringBuilder output = new StringBuilder();
                for (int x = 0; x < WIDTH; x++) {
                    for (int y =0; y < HEIGHT; y++) {
                        index = iterations * lengthY + (x - )
                    }
                        output.append(allMandelbrots.get(currentThread).get(x) + " ");
                    }
                if (currentThread == threads - 1) {
                    currentThread = -1;
                    iterations++;
                }
                currentThread++;
            }
        }
    }
}
