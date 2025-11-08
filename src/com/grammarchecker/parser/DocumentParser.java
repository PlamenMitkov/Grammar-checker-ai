package com.grammarchecker.parser;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Document parser for Word and PDF files
 * Extracts text content without modifying the original files
 */
public class DocumentParser {
    
    /**
     * Parse a document file and extract its text content
     * @param file The file to parse (supports .docx, .pdf, .txt)
     * @return Extracted text content
     * @throws IOException if file cannot be read
     */
    public String parseDocument(File file) throws IOException {
        String fileName = file.getName().toLowerCase();
        
        if (fileName.endsWith(".docx") || fileName.endsWith(".doc")) {
            return parseWordDocument(file);
        } else if (fileName.endsWith(".pdf")) {
            return parsePdfDocument(file);
        } else if (fileName.endsWith(".txt")) {
            return parseTextFile(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Supported: .docx, .pdf, .txt");
        }
    }
    
    /**
     * Extract text from Word document (DOCX)
     */
    private String parseWordDocument(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            
            return extractor.getText();
        }
    }
    
    /**
     * Extract text from PDF document
     */
    private String parsePdfDocument(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
    
    /**
     * Read plain text file
     */
    private String parseTextFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                content.append(new String(buffer, 0, bytesRead, "UTF-8"));
            }
        }
        return content.toString();
    }
    
    /**
     * Check if file format is supported
     */
    public boolean isSupportedFormat(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".docx") || 
               fileName.endsWith(".doc") || 
               fileName.endsWith(".pdf") || 
               fileName.endsWith(".txt");
    }
}
