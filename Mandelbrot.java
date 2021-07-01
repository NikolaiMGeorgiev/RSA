import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class Mandelbrot implements Callable<List<Integer>> {

    private static final int MAX_ITERATIONS = 256;

    int lengthX;
    int lengthY;
    int g;
    int threads;
    int threadId;

    public Mandelbrot(int lengthX, int lengthY, int g, int threads, int threadId) {
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.g = g;
        this.threads = threads;
        this.threadId = threadId;
    }

    @Override
    public List<Integer> call() throws Exception {
        List<Integer> pixels = new ArrayList<>();
        int currentThread = 0;
        for (int bx = 0; bx < threads; bx++) {
            for (int by = 0; by < g; by++) {
                if (currentThread == threadId) {
                    if (threadId == 1) {
                        //System.out.println("bx: " + bx + " by: " + by + " tr: " + currentThread + " id" + threadId);
                    }
                    for (int x = bx * lengthX; x < lengthX * (bx + 1); x++) {
                        for (int y = by * lengthY; y < lengthY * (by + 1); y++) {
                            if (threadId == 1 ){
                                //System.out.println("id: " + threadId + " x: " + x + " y: " + y);
                            }
                            pixels.add(calculatePixel(x, y));
                        }
                    }
                }
                if (currentThread == threads - 1) {
                    currentThread = -1;
                }
                currentThread++;
            }
        }

        System.out.println("Finished mandelbrot with id = " + threadId);
        return pixels;
    }

    public int calculatePixel(int x, int y) {
        double x0 = x * 3.5 / MultithreadingExample.WIDTH - 2.5;
        double y0 = y * 2.0 / MultithreadingExample.HEIGHT - 1.0;
        double ix = 0;
        double iy = 0;
        int iteration = 0;

        while (ix * ix + iy * iy < 4.0 && iteration < MAX_ITERATIONS) {
            double xTemp = ix * ix - iy * iy + x0;
            iy = 2 * ix * iy + y0;
            ix = xTemp;
            iteration++;
        }

        return iteration;
    }
}
