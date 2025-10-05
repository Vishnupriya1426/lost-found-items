package com.myorg.lostfound.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TF-IDF Vectorizer for text similarity computation
 */
public class TextVectorizer {
    
    private Map<String, Double> idfMap;
    private int totalDocuments;
    
    public TextVectorizer() {
        this.idfMap = new HashMap<>();
        this.totalDocuments = 0;
    }
    
    /**
     * Fit the vectorizer on a collection of documents
     */
    public void fit(List<String> documents) {
        this.totalDocuments = documents.size();
        this.idfMap = computeIdf(documents);
    }
    
    /**
     * Transform a single document into TF-IDF vector
     */
    public Map<String, Double> transform(String document) {
        if (document == null || document.trim().isEmpty()) {
            return new HashMap<>();
        }
        
        Map<String, Integer> termFreq = computeTermFrequency(document);
        Map<String, Double> tfIdfVector = new HashMap<>();
        
        for (Map.Entry<String, Integer> entry : termFreq.entrySet()) {
            String term = entry.getKey();
            int tf = entry.getValue();
            double idf = idfMap.getOrDefault(term, 0.0);
            
            // TF-IDF = TF * IDF
            double tfIdf = tf * idf;
            tfIdfVector.put(term, tfIdf);
        }
        
        return tfIdfVector;
    }
    
    /**
     * Transform multiple documents into TF-IDF vectors
     */
    public List<Map<String, Double>> transform(List<String> documents) {
        return documents.stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }
    
    /**
     * Compute Term Frequency (TF) for a document
     */
    private Map<String, Integer> computeTermFrequency(String document) {
        Map<String, Integer> termFreq = new HashMap<>();
        
        // Tokenize and normalize the document
        List<String> tokens = tokenizeAndNormalize(document);
        
        // Count term frequencies
        for (String token : tokens) {
            termFreq.put(token, termFreq.getOrDefault(token, 0) + 1);
        }
        
        return termFreq;
    }
    
    /**
     * Compute Inverse Document Frequency (IDF) for all terms
     */
    private Map<String, Double> computeIdf(List<String> documents) {
        Map<String, Integer> documentFrequency = new HashMap<>();
        
        // Count how many documents contain each term
        for (String document : documents) {
            if (document == null || document.trim().isEmpty()) {
                continue;
            }
            
            Set<String> uniqueTerms = new HashSet<>(tokenizeAndNormalize(document));
            for (String term : uniqueTerms) {
                documentFrequency.put(term, documentFrequency.getOrDefault(term, 0) + 1);
            }
        }
        
        // Compute IDF for each term
        Map<String, Double> idfMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : documentFrequency.entrySet()) {
            String term = entry.getKey();
            int docFreq = entry.getValue();
            
            // IDF = log(total_documents / document_frequency)
            double idf = Math.log((double) totalDocuments / docFreq);
            idfMap.put(term, idf);
        }
        
        return idfMap;
    }
    
    /**
     * Tokenize and normalize text
     */
    private List<String> tokenizeAndNormalize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return Arrays.stream(text.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", " ") // Replace punctuation with spaces
                .split("\\s+"))
                .filter(word -> word.length() > 2) // Filter out short words
                .collect(Collectors.toList());
    }
    
    /**
     * Get the IDF map (for debugging/inspection)
     */
    public Map<String, Double> getIdfMap() {
        return new HashMap<>(idfMap);
    }
    
    /**
     * Get total number of documents used for fitting
     */
    public int getTotalDocuments() {
        return totalDocuments;
    }
}

