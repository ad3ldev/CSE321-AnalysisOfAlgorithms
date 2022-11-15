package sorting;

public class InsertionSort {
    public void insertionSort(int[] a, int n) {
        for (int j = 1; j < n; j++) {
            int key = a[j];
            int i = j-1;
            while(i>=0 && a[i]>key){
                a[i+1]=a[i];
                i=i-1;
            }
            a[i+1]=key;
        }
    }
}
