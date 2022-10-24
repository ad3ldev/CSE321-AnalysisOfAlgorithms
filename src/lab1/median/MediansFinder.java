package lab1.median;

import java.util.*;

public class MediansFinder {
    public double randomizedDivideAndConquer(double[] array) {
        return 0;
    }

    public double medianOfMedians(double[] array) {
        int size = array.length;
        int numberOfArrays = Math.ceilDiv(size,5);
        List<List<Double>> dividedArrays = new ArrayList<>();
        int x = 0;
        int y = 0;
        for (double v : array) {
            if (y == 0) {
                dividedArrays.add(new ArrayList<>());
            }
            dividedArrays.get(x).add(v);
            Collections.sort(dividedArrays.get(x));
            y++;
            if (y == 5) {
                x++;
                y = 0;
            }
        }
        double[] medians = new double[numberOfArrays];
        int i = 0;
        for(List<Double> list : dividedArrays){
            if(list.size()>3){
                medians[i] = list.get(2);
            }
            else if(list.size()==2){
                medians[i] = (list.get(0) + list.get(1))/2;
            }
            else{
                medians[i] = list.get(0);
            }
            i++;
        }
        Arrays.sort(medians);
        return selectMedian(medians);
    }

    private double selectMedian(double[] medians) {
        return 0;
    }

    public double naive(double[] array) {
        Arrays.sort(array);
        int size = array.length;
        if (size % 2 == 0) {
            return (array[size / 2] + array[size / 2 - 1]) / 2;
        }
        return array[size / 2];
    }
}
