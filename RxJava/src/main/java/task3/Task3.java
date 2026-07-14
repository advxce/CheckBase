package task3;

import java.util.*;

public class Task3 {

    public static void main(String[] args) {
        String txt = "Dimaaaleg";
        HuffmanTree tree = new HuffmanTree(txt);

        tree.printTree();
        tree.generateCodes();
        tree.printMap();

        String encodedStr = tree.encodeString(txt);
        System.out.println("encoded str: "+encodedStr);

        String decodedStr = tree.decodeString(encodedStr);
        System.out.println("decoded str: "+decodedStr);


    }

}


class Node {
    Character ch;
    int count;
    Node left;
    Node right;

    Node(Character value, int count) {
        this.ch = value;
        this.count = count;
    }

    @Override
    public String toString() {
        return ch == null ? "[" + count + "]" : "'" + ch + "' (" + count + ")";
    }
}

class HuffmanTree {
    HashMap<Character, Integer> letters = new HashMap<>();
    PriorityQueue<Node> priorityQueue = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.count, b.count)
    );

    HashMap<Character, String> huffmanCodes = new HashMap<>();

    Node root;

    HuffmanTree(String word) {
        for (Character ch : word.toCharArray()) {
            letters.put(ch, letters.getOrDefault(ch, 0) + 1);
        }

        for (Map.Entry<Character, Integer> map : letters.entrySet()) {
            priorityQueue.add(new Node(map.getKey(), map.getValue()));
        }

        initTree();

    }

    void printMap() {
//        System.out.println(letters);
//        System.out.println("queue: " + priorityQueue);
        System.out.println("codes: "+huffmanCodes);
    }

    void printTree() {
        printTreeRecursive(root, 0);
    }

    private void printTreeRecursive(Node node, int level) {
        if (node == null) {
            return;
        }

        printTreeRecursive(node.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        System.out.println(node);
        printTreeRecursive(node.left, level + 1);
    }



    void initTree() {

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();

            Node parent = new Node(null, left.count + right.count);
            parent.left = left;
            parent.right = right;

            priorityQueue.add(parent);

        }
        this.root = priorityQueue.peek();
    }

    void generateCodes(){
        generateRecursionCodes(root,"");
    }

    void generateRecursionCodes(Node node, String code){
        if(node == null)
            return;

        if(node.ch !=null){
            huffmanCodes.put(node.ch, code);
        }

        generateRecursionCodes(node.left, code+"0");
        generateRecursionCodes(node.right, code+"1");

    }

    String encodeString(String text){
        StringBuilder sb = new StringBuilder();
        for(Character ch : text.toCharArray()){
            sb.append(huffmanCodes.get(ch));
        }

        return sb.toString();
    }

    String decodeString(String text){
        StringBuilder sb = new StringBuilder();
        Node current = root;

        for (char bit : text.toCharArray()){
            if(bit == '0'){
                current = current.left;
            } else {
                current = current.right;
            }
            if(current.ch != null){
                sb.append(current.ch);
                current = root;
            }

        }
        return sb.toString();
    }


}