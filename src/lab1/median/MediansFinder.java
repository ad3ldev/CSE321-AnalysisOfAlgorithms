package lab1.median;

import java.util.*;

public class MediansFinder {
    //    Randomized Quicksort and then choose the median.
    public int randomizedDivideAndConquer(int[] array) {
        randomizedQuicksort(array, 0, array.length-1);
        return array[array.length/2];
    }

    private void randomizedQuicksort(int[] array, int low, int high) {
        if(low < high){
            int partitionIndex = randomPartition(array, low, high);
            if(partitionIndex>low){
                randomizedQuicksort(array, low, partitionIndex-1);
            }if(partitionIndex<high){
                randomizedQuicksort(array, partitionIndex, high);
            }
        }
    }

    private int randomPartition(int[] array, int low, int high) {
        int pivotIndex = (int) (Math.random() * (high -  low + 1)) + low;
        int pivot = array[pivotIndex];
        while(low < high){
            while(low < high && array[low] <= pivot){
                low++;
            }
            while(low < high && array[high] >=pivot){
                high--;
            }
            if(low < high){
                int temp = array[low];
                array[low] = array[high];
                array[high] = temp;
            }
        }
        if(array[low] < pivot){
            int temp = array[pivotIndex];
            array[pivotIndex] = array[low];
            array[low] = temp;
        }
        return low;
    }

    public int medianOfMedians(int[] array, int k) {
        int length = array.length;
        return selectMedian(array, 0, length-1, length/2 + 1);
    }

    private int selectMedian(int[] array, int low, int high, int k) {
        int m = partition(array,low, high);
        int length = m-low+1;
        if(length == k){
            return array[m];
        } else if (length > k) {
            return selectMedian(array, low, m-1, k);
        }
        return selectMedian(array, m+1, high, k-length);
    }

    private int partition(int[] array, int low, int high) {
        int pivot = getPivot(array,low, high);
        while (low < high){
            while(array[low] < pivot){
                low++;
            }
            while(array[high] > pivot){
                high--;
            }
            if(array[low] == array[high]){
                low++;
            }
            else if(low < high){
                int temp = array[low];
                array[low] = array[high];
                array[high] = temp;
            }
        }
        return high;
    }

    private int getPivot(int[] array, int low, int high) {
        if(high-low+1 <= 5){
            Arrays.sort(array);
            return array[array.length/2];
        }
        int[] temp = null;
        int medians[] = new int[Math.ceilDiv(high-low+1,5)];
        int medianIndex = 0;

        while(high >= low){
            temp = new int[Math.min(5, high-low+1)];
            for(int i = 0; i < temp.length && low <= high; i++){
                temp[i] = array[low++];
            }
            Arrays.sort(temp);
            medians[medianIndex] = temp[temp.length/2];
            medianIndex++;
        }
        return getPivot(medians,0, medians.length-1);
    }

    public int naive(int[] array) {
        int size = array.length;
        Arrays.sort(array);
        return array[size / 2];
    }
}
