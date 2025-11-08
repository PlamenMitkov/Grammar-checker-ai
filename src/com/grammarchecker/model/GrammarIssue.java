package com.grammarchecker.model;

/**
 * Represents a grammar issue found in text
 */
public class GrammarIssue {
    private String originalText;
    private String suggestion;
    private String explanation;
    private int position;
    private int length;
    
    public GrammarIssue(String originalText, String suggestion, String explanation, int position, int length) {
        this.originalText = originalText;
        this.suggestion = suggestion;
        this.explanation = explanation;
        this.position = position;
        this.length = length;
    }
    
    public String getOriginalText() {
        return originalText;
    }
    
    public String getSuggestion() {
        return suggestion;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public int getPosition() {
        return position;
    }
    
    public int getLength() {
        return length;
    }
    
    @Override
    public String toString() {
        return String.format("[Position %d] '%s' -> '%s'\nExplanation: %s",
                position, originalText, suggestion, explanation);
    }
}
