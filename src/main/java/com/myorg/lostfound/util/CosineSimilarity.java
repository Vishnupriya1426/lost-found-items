package com.myorg.lostfound.util;

import java.util.Map;
import java.util.Set;

/**
 * Utility class for computing cosine similarity between vectors
 */
public class CosineSimilarity {
    
    /**
     * Compute cosine similarity between two vectors
     * 
     * @param vectorA First vector
     * @param vectorB Second vector
     * @return Cosine similarity value between 0 and 1
     */
    public static double compute(Map<String, Double> vectorA, Map<String, Double> vectorB) {
        if (vectorA == null || vectorB == null || vectorA.isEmpty() || vectorB.isEmpty()) {
            return 0.0;
        }
        
        // Get all unique terms from both vectors
        Set<String> allTerms = vectorA.keySet();
        allTerms.addAll(vectorB.keySet());
        
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        
        // Compute dot product and norms
        for (String term : allTerms) {
            double valueA = vectorA.getOrDefault(term, 0.0);
            double valueB = vectorB.getOrDefault(term, 0.0);
            
            dotProduct += valueA * valueB;
            normA += valueA * valueA;
            normB += valueB * valueB;
        }
        
        // Avoid division by zero
        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }
        
        // Cosine similarity = dot product / (normA * normB)
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    
    /**
     * Compute cosine similarity with a threshold check
     * 
     * @param vectorA First vector
     * @param vectorB Second vector
     * @param threshold Minimum similarity threshold
     * @return Cosine similarity if above threshold, 0.0 otherwise
     */
    public static double computeWithThreshold(Map<String, Double> vectorA, Map<String, Double> vectorB, double threshold) {
        double similarity = compute(vectorA, vectorB);
        return similarity >= threshold ? similarity : 0.0;
    }
    
    /**
     * Check if two vectors are similar above a threshold
     * 
     * @param vectorA First vector
     * @param vectorB Second vector
     * @param threshold Similarity threshold
     * @return true if similarity is above threshold
     */
    public static boolean isSimilar(Map<String, Double> vectorA, Map<String, Double> vectorB, double threshold) {
        return compute(vectorA, vectorB) >= threshold;
    }
}
