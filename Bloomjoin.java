import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Bloomjoin {

    // Bloom filter size
    private static final int FILTER_SIZE = 10;

    public static void main(String[] args) {
        // Sample relations (tables)
        List<String> relation1 = new ArrayList<>();
        List<String> relation2 = new ArrayList<>();

        // Fill relations with sample data
        // Format: "id, name"
        relation1.add("1, Animesh");
        relation1.add("2, Ranveer");
        relation1.add("3, Krishna");
        relation1.add("4, Ananya");
        relation1.add("5, Anjali");

        // Format: "id, branch"
        relation2.add("1, Accounting");
        relation2.add("2, HR");
        relation2.add("4, IT");
        relation2.add("6, Marketing");
        relation2.add("7, Sales");

        // Perform Bloom join
        List<String> result = bloomJoin(relation1, relation2);

        // Display result of the join
        displayJoinResult(result);
    }

    public static List<String> bloomJoin(List<String> relation1, List<String> relation2) {
        // Create Bloom filter for relation 2
        BloomFilter bloomFilter = new BloomFilter(FILTER_SIZE);
        for (String row : relation2) {
            String[] parts = row.split(",");
            String key = parts[0];
            bloomFilter.add(key);
        }

        // Perform join
        List<String> result = new ArrayList<>();
        for (String row : relation1) {
            String[] parts = row.split(",");
            String key = parts[0];
            if (bloomFilter.contains(key)) {
                result.add(row);
            }
        }

        return result;
    }

    public static void displayJoinResult(List<String> result) {
        System.out.println("Result of Bloom Join:");
        System.out.println("ID Name Branch");
        for (String row : result) {
            String[] parts = row.split(",");
            String id = parts[0];
            String name = parts[1].trim();
            if(getBranch(id)=="Unknown") continue;
            System.out.println(id + " " + name + " " + getBranch(id));
        }
    }

    // Function to get branch for a given ID from relation2
    public static String getBranch(String id) {
        if (id.equals("1")) return "Accounting";
        else if (id.equals("2")) return "HR";
        else if (id.equals("4")) return "IT";
        else return "Unknown"; // If unknown, don't include in join
    }

    static class BloomFilter {
        private final BitSet bitSet;
        private final int filterSize;

        public BloomFilter(int filterSize) {
            this.filterSize = filterSize;
            this.bitSet = new BitSet(filterSize);
        }

        public void add(String key) {
            int hash1 = primeHash((key));
            int hash2 = bitwiseHash((key));
            bitSet.set(hash1);
            bitSet.set(hash2);
        }

         // Prime hash function
        private int primeHash(String key) {
            int x = Integer.parseInt(key);
            return (101 * x + 127) % (filterSize); // Example of prime hash function
        }

        // Bitwise hash function
        private int bitwiseHash(String key) {
            int x = Integer.parseInt(key);
            x = ((x >> 16) ^ x) * 0x45d9f3b;
            x = ((x >> 16) ^ x) * 0x45d9f3b;
            x = (x >> 16) ^ x;
            return x % (filterSize); // Ensure hash fits within filter size
        }
        public boolean contains(String key) {
            int hash1 = primeHash(key);
            int hash2 = bitwiseHash(key);
            bitSet.set(hash1);
            bitSet.set(hash2);
            return bitSet.get(hash1) && bitSet.get(hash2);
        }
    }
}
