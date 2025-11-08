package com.grammarchecker.gui;

import com.grammarchecker.model.GrammarIssue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Popup window to display grammar checking results
 * Shows issues without modifying the original text or files
 */
public class ResultsPopup extends JDialog {
    private List<GrammarIssue> issues;
    private String originalText;
    
    public ResultsPopup(JFrame parent, List<GrammarIssue> issues, String originalText) {
        super(parent, "Grammar Check Results", true);
        this.issues = issues;
        this.originalText = originalText;
        
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("Grammar Check Results", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Summary
        String summaryText = issues.isEmpty() 
            ? "✓ No grammar issues found! Your text looks good."
            : "Found " + issues.size() + " grammar issue(s) in your text.";
        JLabel summaryLabel = new JLabel(summaryText, SwingConstants.CENTER);
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        summaryLabel.setForeground(issues.isEmpty() ? Color.GREEN.darker() : Color.ORANGE.darker());
        
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(summaryLabel, BorderLayout.CENTER);
        
        // Results area
        JPanel resultsPanel = createResultsPanel();
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        
        // Add to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        if (issues.isEmpty()) {
            JLabel noIssuesLabel = new JLabel("No issues to display");
            noIssuesLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noIssuesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(20));
            panel.add(noIssuesLabel);
        } else {
            for (int i = 0; i < issues.size(); i++) {
                GrammarIssue issue = issues.get(i);
                JPanel issuePanel = createIssuePanel(issue, i + 1);
                panel.add(issuePanel);
                panel.add(Box.createVerticalStrut(10));
            }
        }
        
        return panel;
    }
    
    private JPanel createIssuePanel(GrammarIssue issue, int number) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(250, 250, 250));
        panel.setMaximumSize(new Dimension(750, 200));
        
        // Issue number and position
        JLabel numberLabel = new JLabel("Issue #" + number + " (Position: " + issue.getPosition() + ")");
        numberLabel.setFont(new Font("Arial", Font.BOLD, 13));
        numberLabel.setForeground(new Color(100, 100, 100));
        
        // Original text
        JPanel originalPanel = new JPanel(new BorderLayout(5, 0));
        originalPanel.setBackground(new Color(250, 250, 250));
        JLabel originalLabel = new JLabel("Original: ");
        originalLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextArea originalText = new JTextArea(issue.getOriginalText());
        originalText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        originalText.setForeground(Color.RED.darker());
        originalText.setBackground(new Color(255, 240, 240));
        originalText.setEditable(false);
        originalText.setLineWrap(true);
        originalText.setWrapStyleWord(true);
        originalText.setBorder(new EmptyBorder(3, 5, 3, 5));
        originalPanel.add(originalLabel, BorderLayout.WEST);
        originalPanel.add(originalText, BorderLayout.CENTER);
        
        // Suggestion
        JPanel suggestionPanel = new JPanel(new BorderLayout(5, 0));
        suggestionPanel.setBackground(new Color(250, 250, 250));
        JLabel suggestionLabel = new JLabel("Suggestion: ");
        suggestionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextArea suggestionText = new JTextArea(issue.getSuggestion());
        suggestionText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        suggestionText.setForeground(Color.GREEN.darker());
        suggestionText.setBackground(new Color(240, 255, 240));
        suggestionText.setEditable(false);
        suggestionText.setLineWrap(true);
        suggestionText.setWrapStyleWord(true);
        suggestionText.setBorder(new EmptyBorder(3, 5, 3, 5));
        suggestionPanel.add(suggestionLabel, BorderLayout.WEST);
        suggestionPanel.add(suggestionText, BorderLayout.CENTER);
        
        // Explanation
        JPanel explanationPanel = new JPanel(new BorderLayout(5, 0));
        explanationPanel.setBackground(new Color(250, 250, 250));
        JLabel explanationLabel = new JLabel("Explanation: ");
        explanationLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextArea explanationText = new JTextArea(issue.getExplanation());
        explanationText.setFont(new Font("Arial", Font.PLAIN, 11));
        explanationText.setForeground(new Color(50, 50, 50));
        explanationText.setBackground(new Color(250, 250, 250));
        explanationText.setEditable(false);
        explanationText.setLineWrap(true);
        explanationText.setWrapStyleWord(true);
        explanationPanel.add(explanationLabel, BorderLayout.WEST);
        explanationPanel.add(explanationText, BorderLayout.CENTER);
        
        // Add all to panel
        panel.add(numberLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(originalPanel);
        panel.add(Box.createVerticalStrut(3));
        panel.add(suggestionPanel);
        panel.add(Box.createVerticalStrut(3));
        panel.add(explanationPanel);
        
        return panel;
    }
}
