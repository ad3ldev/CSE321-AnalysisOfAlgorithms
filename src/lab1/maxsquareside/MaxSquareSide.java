package lab1.maxsquareside;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MaxSquareSide {
    public List<Point[]> readFile(String input) throws IOException {
        Path path = Paths.get(input);
        Scanner scanner = new Scanner(path);
        int T = scanner.nextInt();
        List<Point[]> list = new ArrayList<>();
        for(int i = 0; i < T; i++){
            int n = scanner.nextInt();
            Point[] points = new Point[n];
            for(int j = 0; j < n; j++){
                points[j] = new Point();
                points[j].x = scanner.nextInt();
                points[j].y = scanner.nextInt();
            }
            list.add(points);
        }
        scanner.close();
        return list;
    }
    public int solve(Point[] points){
        int size = partition(points, 0, points.length-1, Integer.MAX_VALUE);
        int lastIndex  = points.length - 1;
        int left = lastIndex / 2;
        int right = lastIndex / 2 + 1;
        size = Math.min(size, comparePoints(points, left, right));
        return size;
    }
    public int partition(Point[] points, int start, int end, int side){
        if(end - start + 1 < 4 && end > start){
            int temp = 0;
            if(end-start+1 == 3){
                temp = Math.min(comparePoints(points, start, end-1), comparePoints(points, start+1, end));
            }else{
                temp = comparePoints(points, start, end);
            }
            if(side >= temp);{
                side = temp;
            }
            return side;
        }
        int left = partition(points, start, end/2, side);
        int right = partition(points, end/2 + 1, end, side);
        side = Math.min(side, Math.min(left, right));
        return side;
    }

    private int comparePoints(Point[] points, int start, int end) {
        int size = Integer.MIN_VALUE;
        for(int i = start; i < end; i++){
            int xSize = Math.abs(points[i].x - points[i+1].x);
            int ySize = Math.abs(points[i].y - points[i+1].y);
            size = Math.max(size, Math.max(xSize, ySize));
        }
        return size;
    }
}
