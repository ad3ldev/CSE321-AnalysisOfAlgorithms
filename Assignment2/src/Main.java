import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static int n = 1;

    private static void getFrequency(HashMap<String, Integer> frequencyMap, Path path, AtomicInteger totalSize) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        char[] key = new char[n];
        int index = 0;
        for (byte part : bytes) {
            key[index++] = (char) part;
            if (index == n) {
                frequencyMap.merge(String.valueOf(key), 1, Integer::sum);
                totalSize.getAndIncrement();
                index = 0;
            }
        }
        if (index != 0) {
            while (index != n) {
                key[index++] = 0;
            }
            frequencyMap.merge(String.valueOf(key), 1, Integer::sum);
        }
    }

    private static void createTree(PriorityQueue<Node<String>> nodes, HashMap<String, Integer> frequencyMap) {
        String internalNodeKey = "";
        frequencyMap.forEach((key, value) -> nodes.add(new Node<>(new Pair<>(key, value))));
        while (nodes.size() != 1) {
            Node<String> node1 = nodes.remove();
            Node<String> node2 = nodes.remove();
            Pair<String, Integer> temp = new Pair<>(internalNodeKey, node1.node.y() + node2.node.y());
            InternalNode<String> node = new InternalNode<>(temp, node1, node2);
            nodes.add(node);
        }
    }

    static void DFS(Node<String> current,
                    HashSet<Node<String>> visited,
                    HashMap<String, String> codeWords, String word) {
        visited.add(current);
        if (current instanceof InternalNode<String> tempNode) {
            if (!visited.contains(tempNode.left)) {
                DFS(tempNode.left, visited, codeWords, word + "0");
            }
            if (!visited.contains(tempNode.right)) {
                DFS(tempNode.right, visited, codeWords, word + "1");
            }
        } else {
            if (!word.equals("")) {
                codeWords.put(current.node.x(), word);
                return;
            }
            codeWords.put(current.node.x(), "0");
        }
    }

    private static HashMap<String, String> getCodeWords(String strPath, AtomicInteger totalSize) throws IOException {
        Path path = Paths.get(strPath);
        HashMap<String, Integer> frequencyMap = new HashMap<>();
        getFrequency(frequencyMap, path, totalSize);

        PriorityQueue<Node<String>> nodes = new PriorityQueue<>();
        createTree(nodes, frequencyMap);

        HashMap<String, String> codeWords = new HashMap<>();
        HashSet<Node<String>> visited = new HashSet<>();
        DFS(nodes.remove(), visited, codeWords, "");

        return codeWords;
    }


    private static void compressHeader(FileOutputStream outputStream, int n, HashMap<String, String> codeWords) throws IOException {
        byte[] header = new byte[4 + 4 + ((n + 8) * codeWords.size())];
        int index = 0;
        for (byte nByte : ByteBuffer.allocate(4).putInt(n).array()) {
            header[index++] = nByte;
        }
        for (byte sizeByte : ByteBuffer.allocate(4).putInt(codeWords.size()).array()) {
            header[index++] = sizeByte;
        }
        for (Map.Entry<String, String> entry : codeWords.entrySet()) {
            String key = entry.getKey();
            String string = entry.getValue();
            int value = Integer.valueOf(string, 2);
            int valueSize = string.length();
            for (int i = 0; i < key.length(); i++) {
                header[index++] = (byte) key.charAt(i);
            }
            for (byte intByte : ByteBuffer.allocate(4).putInt(value).array()) {
                header[index++] = intByte;
            }
            for (byte intByte : ByteBuffer.allocate(4).putInt(valueSize).array()) {
                header[index++] = intByte;
            }
        }
        outputStream.write(header);
    }

    private static byte[] compressCharacters(byte[] chunk, HashMap<String, String> codeWords, int chunkSize, AtomicInteger compressedSize) {
        byte[] compressedBytes = new byte[chunkSize];
        char[] key = new char[n];
        int keyIndex = 0;
        char[] oneByte = new char[8];
        int byteIndex = 0;
        for (byte aByte : chunk) {
            key[keyIndex++] = (char) aByte;
            if (keyIndex == n) {
                char[] value = codeWords.get(String.valueOf(key)).toCharArray();
                for (char c : value) {
                    oneByte[byteIndex++] = c;
                    if (byteIndex == 8) {
                        compressedBytes[compressedSize.getAndIncrement()] = (byte) Integer.parseInt(String.valueOf(oneByte), 2);
                        byteIndex = 0;
                        oneByte = new char[8];
                    }
                }
                keyIndex = 0;
            }
        }
        if (byteIndex != 0) {
            String value = String.valueOf(oneByte, 0, byteIndex);
            compressedBytes[compressedSize.getAndIncrement()] = (byte) Integer.parseInt(value);
        }
        return compressedBytes;
    }

    static void encode(String output, String strPath) throws IOException {
        AtomicInteger totalSize = new AtomicInteger();
        HashMap<String, String> codeWords = getCodeWords(strPath, totalSize);
        int mega500 = 1000_000_000;
        int chunkSize;
        FileOutputStream outputStream = new FileOutputStream(output + ".hc");
        compressHeader(outputStream, n, codeWords);
        FileInputStream inputStream = new FileInputStream(strPath);
        while (totalSize.get() != 0) {
            chunkSize = Math.min(totalSize.get(), mega500);
            byte[] bytes = inputStream.readNBytes(chunkSize);
            AtomicInteger compressedSize = new AtomicInteger(0);
            byte[] compressedCharacters = compressCharacters(bytes, codeWords, chunkSize, compressedSize);
            outputStream.write(compressedCharacters, 0, compressedSize.get());
            totalSize.addAndGet(-chunkSize);
        }
        inputStream.close();
        outputStream.close();
    }


    private static void decode(String output, String strPath) throws IOException {
        Path path = Paths.get(strPath);
        byte[] compressedFile = Files.readAllBytes(path);

        int index = 0;
        int n = ByteBuffer.wrap(new byte[]{compressedFile[index++], compressedFile[index++], compressedFile[index++], compressedFile[index++]}).getInt();
        int sizeCodeBytes = ByteBuffer.wrap(new byte[]{compressedFile[index++], compressedFile[index++], compressedFile[index++], compressedFile[index++]}).getInt();
        byte[] codeBytes = new byte[(n + 8) * sizeCodeBytes];
        for (int i = 0; i < (n + 8) * sizeCodeBytes; ) {
            for (int j = 0; j < n; j++) {
                codeBytes[i + j] = compressedFile[index++];
            }
            i += n;
            codeBytes[i++] = compressedFile[index++];
            codeBytes[i++] = compressedFile[index++];
            codeBytes[i++] = compressedFile[index++];
            codeBytes[i++] = compressedFile[index++];
            codeBytes[i++] = compressedFile[index++];
            codeBytes[i++] = compressedFile[index++];
            codeBytes[i++] = compressedFile[index++];
            codeBytes[i++] = compressedFile[index++];
        }

        HashMap<String, String> codeWords = new HashMap<>();
        for (int i = 0; i < codeBytes.length; ) {
            char[] key = new char[n];
            for (int j = 0; j < n; j++) {
                key[j] = (char) codeBytes[i + j];
            }
            i += n;
            int current = i;
            int valueInt = 0;
            while (i < current + 4) {
                valueInt = (valueInt << 8) + (codeBytes[i++] & 0xFF);
            }
            current = i;
            int size = 0;
            while (i < current + 4) {
                size = (size << 8) + (codeBytes[i++] & 0xFF);
            }
            String value = String.format("%32s", Integer.toBinaryString(valueInt)).replace(' ', '0').substring(32 - size, 32);
            codeWords.put(value, String.valueOf(key));
        }

        String fileName = output.substring(0, output.length() - 3);
        FileOutputStream outputStream = new FileOutputStream(fileName);

        StringBuilder binaryString = new StringBuilder();
        while (index < compressedFile.length) {
            byte current = compressedFile[index++];
            binaryString.append(String.format("%8s", Integer.toBinaryString(current & 0xFF)).replace(' ', '0'));
            for (int i = 0; i < binaryString.length(); i++) {
                String substring = binaryString.substring(0, i);
                if (codeWords.containsKey(substring)) {
                    byte[] out = codeWords.get(substring).getBytes();
                    outputStream.write(out);
                    binaryString.delete(0, i);
                    i = 0;
                }
            }
        }
        outputStream.close();
    }

    public static void main(String[] args) throws IOException {
        /*
        “I acknowledge that I am aware of the academic integrity guidelines of this course, and that I worked on this assignment independently without any unauthorized help”.
         */
        int ID = 17012296;
        String mode = args[0];
        Path absolutePath = Path.of(args[1]);
        if(mode.equals("c")){
            n = Integer.parseInt(args[2]);
        }
        String fileName = String.valueOf(absolutePath.getFileName());

        int endIndex = String.valueOf(absolutePath).length() - fileName.length();
        String substring = String.valueOf(absolutePath).substring(0, endIndex);
        if (mode.equals("c")) {
            String finalFileName = substring + ID + "." + n + "." + fileName;
            long start = System.currentTimeMillis();
            encode(finalFileName, String.valueOf(absolutePath));
            long end = System.currentTimeMillis();
            System.out.println(("Encode:\t") + (end - start));
        } else if (mode.equals("d")) {
            String finalFileName = substring + "extracted." + fileName;

            long start = System.currentTimeMillis();
            decode(finalFileName, String.valueOf(absolutePath));
            long end = System.currentTimeMillis();
            System.out.println(("Decode:\t") + (end - start));
        }


    }
}