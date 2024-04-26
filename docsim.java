import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class docsim {

    public static void main(String[] args) {
        // Sample documents
        String[] documents = {
            "agent james bond good agent",
            "james bond madison movie",
            "spy movie daniel craig"
        };
        
        // Create document vectors
        Map<Integer, Map<String, Integer>> documentVectors = createDocumentVectors(documents);
        
        // Display document vectors
        displayDocumentVectors(documentVectors);
        
        // Take input query document from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the query document: ");
        String queryDocument = scanner.nextLine();
        
        // Create document vector for the query document
        Map<String, Integer> queryVector = createDocumentVector(queryDocument);
        
        // Compare query document with each document and output similarity value
        for (int docId : documentVectors.keySet()) {
            int dotProduct = calculateDotProduct(queryVector, documentVectors.get(docId));
            System.out.println("Similarity between query document and document " + docId + ": " + dotProduct);
        }
        
        scanner.close();
    }
    
    // Function to create document vectors
    public static Map<Integer, Map<String, Integer>> createDocumentVectors(String[] documents) {
        Map<Integer, Map<String, Integer>> documentVectors = new HashMap<>();
        int documentId = 1;
        
        for (String document : documents) {
            Map<String, Integer> vector = createDocumentVector(document);
            documentVectors.put(documentId, vector);
            documentId++;
        }
        
        return documentVectors;
    }
    
    // Function to create document vector for a single document
    public static Map<String, Integer> createDocumentVector(String document) {
        Map<String, Integer> vector = new HashMap<>();
        String[] words = document.toLowerCase().split("\\s+");
        
        // Count word frequencies
        for (String word : words) {
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }
        
        return vector;
    }
    
    // Function to calculate dot product between two document vectors
    public static int calculateDotProduct(Map<String, Integer> vector1, Map<String, Integer> vector2) {
        int dotProduct = 0;
        
        // Calculate dot product
        for (Map.Entry<String, Integer> entry : vector1.entrySet()) {
            String word = entry.getKey();
            int frequency1 = entry.getValue();
            int frequency2 = vector2.getOrDefault(word, 0);
            
            dotProduct += frequency1 * frequency2;
        }
        
        return dotProduct;
    }
    
    // Function to display document vectors in tabular format
    public static void displayDocumentVectors(Map<Integer, Map<String, Integer>> documentVectors) {
        // Get unique words across all documents
        Set<String> uniqueWords = new HashSet<>();
        for (Map<String, Integer> vector : documentVectors.values()) {
            uniqueWords.addAll(vector.keySet());
        }
        
        // Display header
        System.out.print("Document id\t");
        for (String word : uniqueWords) {
            System.out.print(word + "\t");
        }
        System.out.println();
        
        // Display vectors
        for (Map.Entry<Integer, Map<String, Integer>> entry : documentVectors.entrySet()) {
            int documentId = entry.getKey();
            Map<String, Integer> vector = entry.getValue();
            
            System.out.print(documentId + "\t\t");
            for (String word : uniqueWords) {
                int frequency = vector.getOrDefault(word, 0);
                System.out.print(frequency + "\t");
            }
            System.out.println();
        }
    }
}