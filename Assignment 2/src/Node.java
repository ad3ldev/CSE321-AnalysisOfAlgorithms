
public class Node implements Comparable<Node>{
    Pair<Byte, Integer> node;
    public Node(Pair<Byte, Integer> node){
        this.node = node;
    }

    @Override
    public int compareTo(Node o) {
        return this.node.y().compareTo(o.node.y());
    }
}
