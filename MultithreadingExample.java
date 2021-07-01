import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultithreadingExample {

   // public static final int WIDTH = 3840;
    //public static final int HEIGHT = 2160;
    public static final int WIDTH = 100;
    public static final int HEIGHT = 60;
    private static final int ALL_PIXELS = WIDTH * HEIGHT;

    /*
    private static final double STARTX = -1.5;
    private static final double ENDX = 0.5;
    private static final double STARTY = -1;
    */

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Instant start = Instant.now();
        //final int THREADS = Integer.parseInt(args[0]);
        //final int G = Integer.parseInt(args[1]);
        final int THREADS = 4;
        final int G = 5;
        int sectorWidth = WIDTH / THREADS;
        int sectorHeight = HEIGHT / G;

        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

        List<Future<List<Integer>>> futures = new ArrayList<>();

        for (int thread = 0; thread < THREADS; thread++) {
            int startY = thread * sectorHeight;
            int startX = thread * sectorWidth;
            futures.add(executorService.submit(new Mandelbrot(sectorWidth, sectorHeight, G, THREADS, thread)));
        }


        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Took " + start.until(Instant.now(), ChronoUnit.MILLIS) + "ms");

        List<List<Integer>> allMandelbrots = new ArrayList<>();
        for (Future<List<Integer>> future : futures) {
            allMandelbrots.add(future.get());
        }
        drawMandelbrots(allMandelbrots, sectorWidth, sectorHeight, THREADS, G);
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
        int i = 0;
        int[][] matrix = new int[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            StringBuilder output = new StringBuilder();
            for (int y = 0; y < HEIGHT; y++) {
                index = iterations * lengthY + i;
                //System.out.println(currentThread + "| indx: " + index + " iter:" + iterations + " i: " + i + "L:" + lengthY + " H: " + y + " W:" + x);
                output.append(allMandelbrots.get(currentThread).get(index) + " ");
                matrix[x][y] = allMandelbrots.get(currentThread).get(index);
                i++;

                if (i == lengthY) {
                    //System.out.println("s: " + allMandelbrots.get(currentThread).size());
                    currentThread++;
                    i = 0;
                }
                if (currentThread == threads) {
                    currentThread = 0;
                    iterations++;
                }

            }
            //System.out.println(output.toString());
        }

        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                /*
                int rgb = matrix[x][y] - 1;
                rgb = (rgb << 8) + matrix[x][y] - 1;
                rgb = (rgb << 8) + matrix[x][y] - 1;
                */

                matrix[x][y] -= 1;
				if (matrix[x][y] == 0) { // inside ...
					bi.setRGB(x, y, 0x00ff00);
				} else if (matrix[x][y] <= 10) { // outside ... (rapid move)
					bi.setRGB(x, y, 0xFFFFFF);
				// close to inside cases ...
				// } else if (10 < matrix[x][y] && matrix[x][y] <= 50) {
				// bi.setRGB(x, y, 0x0033EE);
				} else if (matrix[x][y] == 11) {
					bi.setRGB(x, y, 0x0000ff);
				} else if (matrix[x][y] == 12) {
					bi.setRGB(x, y, 0x0000ee);
				} else if (matrix[x][y] == 13) {
					bi.setRGB(x, y, 0x0000dd);
				} else if (matrix[x][y] == 14) {
					bi.setRGB(x, y, 0x0000cc);
				} else if (matrix[x][y] == 15) {
					bi.setRGB(x, y, 0x0000bb);
				} else if (matrix[x][y] == 16) {
					bi.setRGB(x, y, 0x0000aa);
				} else if (matrix[x][y] == 17) {
					bi.setRGB(x, y, 0x000099);
				} else if (matrix[x][y] == 18) {
					bi.setRGB(x, y, 0x000088);
				} else if (matrix[x][y] == 19) {
					bi.setRGB(x, y, 0x000077);
				} else if (matrix[x][y] == 20) {
					bi.setRGB(x, y, 0x000066);
				} else if (20 < matrix[x][y] && matrix[x][y] <= 30) {
					bi.setRGB(x, y, 0x666600);
				} else if (30 < matrix[x][y] && matrix[x][y] <= 40) {
					bi.setRGB(x, y, 0x777700);
				} else if (40 < matrix[x][y] && matrix[x][y] <= 50) {
					bi.setRGB(x, y, 0x888800);
				} else if (50 < matrix[x][y] && matrix[x][y] <= 100) {
					bi.setRGB(x, y, 0x999900);
				} else if (100 < matrix[x][y] && matrix[x][y] <= 150) {
					bi.setRGB(x, y, 0xaaaa00);
				} else if (150 < matrix[x][y] && matrix[x][y] <= 200) {
					bi.setRGB(x, y, 0xbbbb00);
				} else if (200 < matrix[x][y] && matrix[x][y] <= 250) {
					bi.setRGB(x, y, 0xcccc00);
				} else {
					bi.setRGB(x, y, 0xeeee00);
				}

                //System.out.print(matrix[x][y]);
            }
            //System.out.println();
        }

        try {
            ImageIO.write(bi, "PNG", new File("mandelbrot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
