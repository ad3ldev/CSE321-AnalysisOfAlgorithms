import java.util.List;

public class Node<T> implements Comparable<Node> {
    Pair<T, Integer> node;

    public Node(Pair<T, Integer> node) {
        this.node = node;
    }

    @Override
    public int compareTo(Node o) {
        return this.node.y().compareTo((Integer) o.node.y());
    }
}
