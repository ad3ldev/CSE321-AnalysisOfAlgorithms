import lab1.maxsquareside.MaxSquareSide;
import lab1.median.MediansFinder;
import lab2.Job;
import lab2.WeightedSchedule;
import sorting.InsertionSort;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static void mediansTest() throws IOException {
        FileWriter fileWriter = new FileWriter("medians-output.txt");
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

    public static void maxSquareSide(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter("maxSquareSide-output.txt");
        MaxSquareSide maxSquareSide = new MaxSquareSide();
        List<Point[]> input = new ArrayList<>(maxSquareSide.readFile(args[0]));
        for (int i = 0; i < input.size(); i++) {
            fileWriter.write(maxSquareSide.solve(input.get(i)) + "\n");
        }
        fileWriter.close();
    }

    public static void insertionSort() {
        InsertionSort insertionSort = new InsertionSort();
        int[] array = {8, 2, 4, 9, 3, 6};
        insertionSort.insertionSort(array, array.length);
    }

    public static void weightedSchedule(String path) throws IOException {
        Scanner scanner = new Scanner(new File(path));
        int i = 0;
        int n = 0;
        if(scanner.hasNextInt()){
            n = scanner.nextInt();
        }
        Job[] jobs = new Job[n];
        while(scanner.hasNextInt())
        {
            jobs[i]= new Job(scanner.nextInt(), scanner.nextInt(),scanner.nextInt());
            i++;
        }
        scanner.close();
        WeightedSchedule weightedSchedule = new WeightedSchedule();
        int maxWeight = weightedSchedule.schedule(jobs);
        String outputPath = path.replace(".in", "_17012296.out");
        FileWriter fileWriter = new FileWriter(outputPath);
        fileWriter.write(maxWeight + "\n");
        fileWriter.close();
    }

    public static void main(String[] args) throws IOException {
        weightedSchedule(args[0]);
    }
}