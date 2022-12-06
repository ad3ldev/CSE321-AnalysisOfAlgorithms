package lab2;

import java.util.Arrays;
import java.util.Comparator;

public class WeightedSchedule {
    // Find the last non-conflicting job
    int binarySearch(Job[] jobs, int index){
        int low = 0, high = index - 1;
        while (low <= high){
            int mid = (low + high) / 2;
            if (jobs[mid].finish <= jobs[index].start) {
                if (jobs[mid + 1].finish <= jobs[index].start){
                    low = mid + 1;
                }
                else{
                    return mid;
                }
            }
            else{
                high = mid - 1;
            }
        }
        return -1;
    }

    // Schedule
    public int schedule(Job[] jobs)
    {
        Arrays.sort(jobs, Comparator.comparingInt(a -> a.finish));
        int n = jobs.length;
        int[] optimal = new int[n];
        optimal[0] = jobs[0].weight;
        for (int i=1; i<n; i++){
            int currentWeight = jobs[i].weight;
            int l = binarySearch(jobs, i);
            if (l != -1){
                currentWeight += optimal[l];
            }
            optimal[i] = Math.max(currentWeight, optimal[i-1]);
        }
        return optimal[n-1];
    }
}
