public class InternalNode extends Node{
    Node left;
    Node right;
    public InternalNode(Pair<Byte, Integer> node, Node left, Node right) {
        super(node);
        this.left = left;
        this.right = right;
    }
}
