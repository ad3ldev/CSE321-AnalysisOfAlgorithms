import lab1.maxsquareside.MaxSquareSide;
import lab1.median.MediansFinder;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {
    private static void mediansTest() throws IOException {
        FileWriter fileWriter = new FileWriter("java-output.txt");
        for (int times = 1; times <= 5; times++) {
            fileWriter.write("PASS: " + times + "\n");
            fileWriter.write("============================================\n");
            Random random = new Random();
            long start1 = 0;
            long end1 = 0;
            long start2 = 0;
            long end2 = 0;
            for (int i = 2; i <= 67_108_864; i *= 2) {
                int[] array = new int[i];
                for (int j = 0; j < array.length; j++) {
                    array[j] = random.nextInt();
                }
                int[] array1 = array.clone();
                int[] array2 = array.clone();
                int[] array3 = array.clone();
                MediansFinder mediansFinder = new MediansFinder();
                fileWriter.write("Array size = " + i + "\n");

                start1 = System.currentTimeMillis();
                fileWriter.write("Median of Medians: " + mediansFinder.medianOfMedians(array1, array1.length / 2) + "\n");
                end1 = System.currentTimeMillis();

                start2 = System.currentTimeMillis();
                fileWriter.write("Randomized Divide and Conquer: " + mediansFinder.randomizedDivideAndConquer(array2) + "\n");
                end2 = System.currentTimeMillis();
                fileWriter.write("Naive (to make sure): " + mediansFinder.naive(array3) + "\n");

                fileWriter.write("Time Taken M-o-M: " + (end1 - start1) + "\n");
                fileWriter.write("Time Taken Randomized: " + (end2 - start2) + "\n");

                fileWriter.write("--------------------------\n");
            }
        }
        fileWriter.close();
    }
    public static void main(String[] args) throws IOException {
//        mediansTest();
        MaxSquareSide maxSquareSide = new MaxSquareSide();
        Point[] trial = maxSquareSide.readFile("input.txt");
        Arrays.sort(trial, (a, b) -> {
            int xComp = Integer.compare(a.x, b.x);
            if (xComp == 0)
                return Integer.compare(a.y, b.y);
            else
                return xComp;
        });
        System.out.println(maxSquareSide.solve(trial));
    }
}