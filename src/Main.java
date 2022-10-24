import lab1.median.MediansFinder;

public class Main {
    public static void main(String[] args) {
        double[] array = {329,1,2319,293,913,2,3,4,5,6,7,8,9,10,11,12,13,14};
        MediansFinder mediansFinder =  new MediansFinder();
        mediansFinder.medianOfMedians(array);
        System.out.println(mediansFinder.naive(array));
    }
}