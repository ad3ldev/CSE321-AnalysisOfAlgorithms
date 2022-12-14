import java.beans.Visibility;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        HashMap<Byte, Integer> frequancyMap = new HashMap<Byte, Integer>();
        Path path = Paths.get("/Users/adele/Desktop/College/CSE321-AnalysisOfAlgorithms/Assignment 2/src/Algorithms - Lectures 7 and 8 (Greedy algorithms).pdf");
        byte[] data = Files.readAllBytes(path);
        for (byte part : data) {
            frequancyMap.merge(part, 1, Integer::sum);
        }
        PriorityQueue<Node> nodes = new PriorityQueue<>();
        frequancyMap.forEach((key, value) -> {
            nodes.add(new Node(new Pair<>(key, value)));
        });
        while (nodes.size() != 1) {
            Node node1 = nodes.remove();
            Node node2 = nodes.remove();
            Pair<Byte, Integer> temp = new Pair<>((byte) (node1.node.x() & node2.node.x()), node1.node.y() + node2.node.y());
            InternalNode node = new InternalNode(temp, node1, node2);
            nodes.add(node);
        }
        HashMap<Byte, Integer> codeWords = new HashMap<>();
        HashSet<Node> visited = new HashSet<>();
        DFS(nodes.remove(),visited,codeWords,0);
        HashSet<Integer> trial = new HashSet<>();
        codeWords.forEach((key, value) -> {
            if(trial.contains(key)){
                System.out.println("PANIC");
            }else{
                trial.add((int)key);
            }
        });
//        System.out.println(codeWords);
    }

    static void DFS(Node current,
                    HashSet<Node> visited,
                    HashMap<Byte, Integer> codeWords, int word) {
        visited.add(current);
        if(current instanceof InternalNode tempNode){
            if (!visited.contains(tempNode.left)) {
                DFS(tempNode.left,visited,codeWords,word << 1);
            }
            if (!visited.contains(tempNode.right)) {
                DFS(tempNode.right, visited, codeWords,  (word << 1) | 1);
            }
        }else{
            codeWords.put(current.node.x(), word);
        }
    }
}