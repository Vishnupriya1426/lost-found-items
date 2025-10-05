package com.myorg.lostfound.util;

import java.util.*;

/**
 * Demo class to showcase TF-IDF and cosine similarity functionality
 */
public class TextSimilarityDemo {
    
    public static void main(String[] args) {
        // Sample documents for demonstration
        List<String> documents = Arrays.asList(
            "I lost my black iPhone 12 near the coffee shop downtown",
            "Found a black iPhone near the downtown coffee shop yesterday",
            "Lost wallet with credit cards and driver's license",
            "Found wallet containing credit cards and ID",
            "Lost my keys in the parking lot",
            "Found keys in the parking area near the mall"
        );
        
        // Initialize vectorizer
        TextVectorizer vectorizer = new TextVectorizer();
        vectorizer.fit(documents);
        
        System.out.println("=== TF-IDF Vectorizer Demo ===");
        System.out.println("Total documents: " + vectorizer.getTotalDocuments());
        System.out.println("IDF Map size: " + vectorizer.getIdfMap().size());
        System.out.println();
        
        // Test similarity between similar documents
        String doc1 = "I lost my black iPhone 12 near the coffee shop downtown";
        String doc2 = "Found a black iPhone near the downtown coffee shop yesterday";
        
        System.out.println("Document 1: " + doc1);
        System.out.println("Document 2: " + doc2);
        System.out.println();
        
        // Calculate TF-IDF vectors
        Map<String, Double> vector1 = vectorizer.transform(doc1);
        Map<String, Double> vector2 = vectorizer.transform(doc2);
        
        System.out.println("TF-IDF Vector 1 (top 10 terms):");
        vector1.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(10)
            .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + String.format("%.4f", entry.getValue())));
        
        System.out.println();
        System.out.println("TF-IDF Vector 2 (top 10 terms):");
        vector2.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(10)
            .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + String.format("%.4f", entry.getValue())));
        
        System.out.println();
        
        // Calculate cosine similarity
        double cosineSimilarity = CosineSimilarity.compute(vector1, vector2);
        System.out.println("Cosine Similarity: " + String.format("%.4f", cosineSimilarity));
        
        // Test with different documents
        System.out.println("\n=== Testing Different Document Pairs ===");
        
        String doc3 = "Lost wallet with credit cards and driver's license";
        String doc4 = "Found keys in the parking area near the mall";
        
        Map<String, Double> vector3 = vectorizer.transform(doc3);
        Map<String, Double> vector4 = vectorizer.transform(doc4);
        
        double similarity3_4 = CosineSimilarity.compute(vector3, vector4);
        System.out.println("Document 3: " + doc3);
        System.out.println("Document 4: " + doc4);
        System.out.println("Cosine Similarity: " + String.format("%.4f", similarity3_4));
        
        // Test with similar wallet documents
        String doc5 = "Lost wallet with credit cards and driver's license";
        String doc6 = "Found wallet containing credit cards and ID";
        
        Map<String, Double> vector5 = vectorizer.transform(doc5);
        Map<String, Double> vector6 = vectorizer.transform(doc6);
        
        double similarity5_6 = CosineSimilarity.compute(vector5, vector6);
        System.out.println("\nDocument 5: " + doc5);
        System.out.println("Document 6: " + doc6);
        System.out.println("Cosine Similarity: " + String.format("%.4f", similarity5_6));
        
        // Test threshold functionality
        System.out.println("\n=== Threshold Testing ===");
        double threshold = 0.3;
        boolean isSimilar = CosineSimilarity.isSimilar(vector1, vector2, threshold);
        System.out.println("Are documents 1 and 2 similar above threshold " + threshold + "? " + isSimilar);
        
        boolean isSimilar3_4 = CosineSimilarity.isSimilar(vector3, vector4, threshold);
        System.out.println("Are documents 3 and 4 similar above threshold " + threshold + "? " + isSimilar3_4);
    }
}

