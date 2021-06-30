import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class Mandelbrot implements Callable<List<Integer>> {

    private static final int MAX_ITERATIONS = 256;

    int startX;
    int startY;
    int lengthX;
    int lengthY;
    int g;
    int threads;

    public Mandelbrot(int startX, int startY, int lengthX, int lengthY, int g, int threads) {
        this.startX = startX;
        this.startY = startY;
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.g = g;
        this.threads = threads;
    }

    @Override
    public List<Integer> call() throws Exception {
        List<Integer> pixels = new ArrayList<>();
        int currentThread = 0;
        for (int bx = 0; bx < threads; bx++) {
            for (int by = 0; by < g; by++) {
                if (currentThread == 0) {
                    for (int x = bx * lengthX; x < lengthX * (bx + 1); x++) {
                        for (int y = by * lengthY; y < lengthY * (by + 1); y++) {
                            pixels.add(calculatePixel(x, y));
                        }
                    }
                } else if (currentThread == threads - 1) {
                    currentThread = -1;
                }
                currentThread++;
            }
        }

        System.out.println("Finished mandelbrot at y = " + startY);
        return pixels;
    }

    public int calculatePixel(int x, int y) {
        int ix = 0;
        int iy = 0;
        int iteration = 0;

        while (ix * ix + iy * iy < 4 && iteration < MAX_ITERATIONS) {
            int xTemp = ix * ix - iy * iy + x;
            iy = 2 * ix * iy + y;
            ix = xTemp;
            iteration++;
        }
        return iteration;
    }
}
