package com.grammarchecker.gui;

import com.grammarchecker.model.GrammarIssue;
import com.grammarchecker.parser.DocumentParser;
import com.grammarchecker.service.GrammarCheckService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Main GUI for Grammar Checker Application
 * Displays results in a popup window without modifying files
 */
public class GrammarCheckerGUI extends JFrame {
    private JTextArea textArea;
    private JButton checkButton;
    private JButton loadFileButton;
    private JLabel statusLabel;
    
    private GrammarCheckService grammarService;
    private DocumentParser documentParser;
    
    public GrammarCheckerGUI() {
        grammarService = new GrammarCheckService();
        documentParser = new DocumentParser();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Grammar Checker AI");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadFileButton = new JButton("Load File (Word/PDF/TXT)");
        checkButton = new JButton("Check Grammar");
        
        loadFileButton.setFont(new Font("Arial", Font.PLAIN, 14));
        checkButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        topPanel.add(loadFileButton);
        topPanel.add(checkButton);
        
        // Text area for input
        JLabel inputLabel = new JLabel("Text to check:");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(850, 500));
        
        // Status label
        statusLabel = new JLabel("Ready. Load a file or paste text to check grammar.");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.BLUE);
        
        // Add components to main panel
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(inputLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        loadFileButton.addActionListener(new LoadFileListener());
        checkButton.addActionListener(new CheckGrammarListener());
    }
    
    /**
     * Listener for loading files
     */
    private class LoadFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a document to check");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    return documentParser.isSupportedFormat(f);
                }
                
                @Override
                public String getDescription() {
                    return "Supported Documents (*.docx, *.pdf, *.txt)";
                }
            });
            
            int result = fileChooser.showOpenDialog(GrammarCheckerGUI.this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                loadFile(selectedFile);
            }
        }
    }
    
    /**
     * Load and parse a file
     */
    private void loadFile(File file) {
        try {
            statusLabel.setText("Loading file: " + file.getName() + "...");
            statusLabel.setForeground(Color.BLUE);
            
            String content = documentParser.parseDocument(file);
            textArea.setText(content);
            
            statusLabel.setText("File loaded successfully: " + file.getName() + 
                              " (" + content.length() + " characters)");
            statusLabel.setForeground(Color.GREEN.darker());
            
        } catch (Exception ex) {
            statusLabel.setText("Error loading file: " + ex.getMessage());
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this,
                "Failed to load file:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Listener for checking grammar
     */
    private class CheckGrammarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textArea.getText();
            
            if (text == null || text.trim().isEmpty()) {
                JOptionPane.showMessageDialog(GrammarCheckerGUI.this,
                    "Please enter some text or load a file first.",
                    "No Text",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Run grammar check in background thread
            SwingWorker<List<GrammarIssue>, Void> worker = new SwingWorker<>() {
                @Override
                protected List<GrammarIssue> doInBackground() {
                    statusLabel.setText("Checking grammar... Please wait.");
                    statusLabel.setForeground(Color.ORANGE.darker());
                    checkButton.setEnabled(false);
                    return grammarService.checkGrammar(text);
                }
                
                @Override
                protected void done() {
                    try {
                        List<GrammarIssue> issues = get();
                        checkButton.setEnabled(true);
                        showResultsPopup(issues, text);
                        
                        if (issues.isEmpty()) {
                            statusLabel.setText("No grammar issues found!");
                            statusLabel.setForeground(Color.GREEN.darker());
                        } else {
                            statusLabel.setText("Found " + issues.size() + " grammar issue(s).");
                            statusLabel.setForeground(Color.ORANGE.darker());
                        }
                    } catch (Exception ex) {
                        checkButton.setEnabled(true);
                        statusLabel.setText("Error: " + ex.getMessage());
                        statusLabel.setForeground(Color.RED);
                        JOptionPane.showMessageDialog(GrammarCheckerGUI.this,
                            "Error checking grammar:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }
    
    /**
     * Show grammar check results in a popup window
     */
    private void showResultsPopup(List<GrammarIssue> issues, String originalText) {
        ResultsPopup popup = new ResultsPopup(this, issues, originalText);
        popup.setVisible(true);
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            GrammarCheckerGUI gui = new GrammarCheckerGUI();
            gui.setVisible(true);
        });
    }
}
