import java.util.List;

public class InternalNode<T> extends Node<T>{
    Node<T> left;
    Node<T> right;
    public InternalNode(Pair<T, Integer> node, Node<T> left, Node<T> right) {
        super(node);
        this.left = left;
        this.right = right;
    }
}
