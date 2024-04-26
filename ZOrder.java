import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ZOrder {
    // Convert 2D coordinates to Z-order value
    public static int toZOrder(String x, String y) {
        int value = 0;
        int n = x.length();
        for (int i = 0; i < n; i++) {
            if (x.charAt(i) == '1') {
                value = (value << 1) | 1;
            } else {
                value = (value << 1) | 0;
            }
            if (y.charAt(i) == '1') {
                value = (value << 1) | 1;
            } else {
                value = (value << 1) | 0;
            }
        }
        return value;
    }

    public static int[] fromZOrder(int value, int n) {
        int[] result = new int[2];
        int x = 0, y = 0;

        for (int i = 0; i < n; i++) {
            x |= (value & 1) << i;
            value >>= 1;
            y |= (value & 1) << i;
            value >>= 1;
        }

        result[0] = x;
        result[1] = y;

        return result;
    }

    public static String getBits(int value) {
        String bits = "";
        boolean leadingZeros = true;
        for (int i = 31; i >= 0; i--) {
            if ((value & (1 << i)) != 0) {
                bits += "1";
                leadingZeros = false;
            } else if (!leadingZeros) {
                bits += "0";
            }
        }
        return bits.isEmpty() ? "0" : bits;
    }

    public static void printPoints(HashMap<Integer, String> map) {
        // Init grid to '.'
        char[][] grid = new char[8][8];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = '.';
            }
        }

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            int zOrder = entry.getKey();
            int[] coordinates = fromZOrder(zOrder, 3); // get coordinates from ZOrder value
            int x = coordinates[0];
            int y = coordinates[1];
            grid[7 - x][y] = 'o'; // adjust x-axis for proper visualization
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            int zOrder = entry.getKey();
            String binary = entry.getValue();
            System.out.println("ZOrder: " + binary + ", Decimal: " + zOrder);
        }
    }

    public static boolean containsValue(HashMap<Integer, String> map, String value) {
        return map.containsValue(value);
    }

    public static void main(String[] args) {

        // Considering 3-bit values
        HashMap<Integer, String> zIndex = new HashMap<>();

        // Adding points to the index
        int point1 = toZOrder("110", "010"); // (6,2)
        zIndex.put(point1, getBits(point1));

        int point2 = toZOrder("101", "001"); // (5,1)
        zIndex.put(point2, getBits(point2));

        int point3 = toZOrder("011", "111"); // (3,7)
        zIndex.put(point3, getBits(point3));

        int point4 = toZOrder("001", "100"); // (1,4)
        zIndex.put(point4, getBits(point4));

        int point5 = toZOrder("100", "111"); // (1,4)
        zIndex.put(point5, getBits(point5));

        // Get all points stored
        printPoints(zIndex);

        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter point to search: ");
        int pointToSearch1 = sc.nextInt();
        System.out.println(containsValue(zIndex, getBits(pointToSearch1)));

        System.out.print("\nEnter point to search: ");
        int pointToSearch2 = sc.nextInt();
        System.out.println(containsValue(zIndex, getBits(pointToSearch2)));

        sc.close();

    }
}
