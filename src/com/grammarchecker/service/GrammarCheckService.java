package com.grammarchecker.service;

import com.grammarchecker.config.Config;
import com.grammarchecker.model.GrammarIssue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Grammar checking service using OpenAI API
 * This service analyzes text and returns grammar suggestions without modifying the original
 */
public class GrammarCheckService {
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    
    /**
     * Check grammar for the provided text
     * @param text The text to check
     * @return List of grammar issues found
     */
    public List<GrammarIssue> checkGrammar(String text) {
        List<GrammarIssue> issues = new ArrayList<>();
        
        if (text == null || text.trim().isEmpty()) {
            return issues;
        }
        
        try {
            String apiKey = Config.getOpenAIApiKey();
            if (apiKey.isEmpty()) {
                throw new IllegalStateException("OpenAI API key not configured. Please set it in .env file");
            }
            
            // Create the prompt for grammar checking
            String prompt = createGrammarCheckPrompt(text);
            
            // Call OpenAI API
            String response = callOpenAI(apiKey, prompt);
            
            // Parse response to extract grammar issues
            issues = parseGrammarIssues(response, text);
            
        } catch (Exception e) {
            System.err.println("Error checking grammar: " + e.getMessage());
            e.printStackTrace();
        }
        
        return issues;
    }
    
    private String createGrammarCheckPrompt(String text) {
        return "You are a grammar and writing assistant. Analyze the following text and identify grammar errors, " +
               "spelling mistakes, punctuation issues, and style improvements. " +
               "For each issue found, provide:\n" +
               "1. The original problematic text\n" +
               "2. A suggested correction\n" +
               "3. A brief explanation of the issue\n" +
               "4. The approximate position (character index) in the text\n\n" +
               "Format your response as a JSON array with objects containing: " +
               "'original', 'suggestion', 'explanation', 'position'.\n\n" +
               "Text to analyze:\n" + text;
    }
    
    private String callOpenAI(String apiKey, String prompt) throws Exception {
        URL url = new URL(OPENAI_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setDoOutput(true);
        
        // Create request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", Config.getOpenAIModel());
        requestBody.put("max_tokens", Config.getMaxTokens());
        
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messages.put(message);
        requestBody.put("messages", messages);
        
        // Send request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Read response
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("OpenAI API returned error code: " + responseCode);
        }
        
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        
        return response.toString();
    }
    
    private List<GrammarIssue> parseGrammarIssues(String apiResponse, String originalText) {
        List<GrammarIssue> issues = new ArrayList<>();
        
        try {
            JSONObject jsonResponse = new JSONObject(apiResponse);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            
            if (choices.length() > 0) {
                String content = choices.getJSONObject(0)
                                       .getJSONObject("message")
                                       .getString("content");
                
                // Try to extract JSON array from the content
                int jsonStart = content.indexOf('[');
                int jsonEnd = content.lastIndexOf(']') + 1;
                
                if (jsonStart != -1 && jsonEnd > jsonStart) {
                    String jsonString = content.substring(jsonStart, jsonEnd);
                    JSONArray issuesArray = new JSONArray(jsonString);
                    
                    for (int i = 0; i < issuesArray.length(); i++) {
                        JSONObject issueObj = issuesArray.getJSONObject(i);
                        
                        String original = issueObj.optString("original", "");
                        String suggestion = issueObj.optString("suggestion", "");
                        String explanation = issueObj.optString("explanation", "");
                        int position = issueObj.optInt("position", 0);
                        
                        GrammarIssue issue = new GrammarIssue(
                            original,
                            suggestion,
                            explanation,
                            position,
                            original.length()
                        );
                        issues.add(issue);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing grammar issues: " + e.getMessage());
        }
        
        return issues;
    }
}
