# Grammar Checker AI

A Java-based grammar checking application that uses OpenAI's API to analyze documents and provide grammar suggestions. The application supports Word documents (.docx), PDFs, and text files, displaying results in a popup window without modifying the original files.

## Features

✅ **Grammar checking logic** using OpenAI API (GPT-4o-mini)
✅ **Secure API key management** via `.env` file (not committed to GitHub)
✅ **Support for larger files**: Word documents (.docx), PDF files, and text files
✅ **Non-destructive checking**: Shows suggestions in a popup window without modifying files
✅ **Handles long text**: Designed to work with extensive documents

## Project Structure

```
Grammar-checker-ai/
├── src/
│   └── com/
│       └── grammarchecker/
│           ├── config/
│           │   └── Config.java              # Environment configuration loader
│           ├── model/
│           │   └── GrammarIssue.java        # Grammar issue data model
│           ├── service/
│           │   └── GrammarCheckService.java # OpenAI API integration
│           ├── parser/
│           │   └── DocumentParser.java      # Word/PDF/TXT file parser
│           └── gui/
│               ├── GrammarCheckerGUI.java   # Main application window
│               └── ResultsPopup.java        # Results display popup
├── .env.example                              # Example environment file
├── .gitignore                                # Git ignore file (includes .env)
└── README.md                                 # This file
```

## Setup Instructions

### 1. Prerequisites

- Java 17 or higher
- Maven (for dependency management)
- OpenAI API key ([Get one here](https://platform.openai.com/api-keys))

### 2. Required Dependencies

Add these dependencies to your project (via Maven `pom.xml`):

```xml
<dependencies>
    <!-- JSON processing -->
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20231013</version>
    </dependency>
    
    <!-- Apache POI for Word documents -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.3</version>
    </dependency>
    
    <!-- PDFBox for PDF documents -->
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>2.0.29</version>
    </dependency>
</dependencies>
```

### 3. Configure Environment

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` and add your OpenAI API key:
   ```
   OPENAI_API_KEY=sk-your-actual-api-key-here
   OPENAI_MODEL=gpt-4o-mini
   MAX_TOKENS=4000
   ```

3. **Important**: The `.env` file is in `.gitignore` and will NOT be committed to GitHub for security.

### 4. Build and Run

```bash
# Compile the project
javac -cp ".:lib/*" src/com/grammarchecker/**/*.java

# Run the application
java -cp ".:lib/*:src" com.grammarchecker.gui.GrammarCheckerGUI
```

Or use your IDE (Eclipse, IntelliJ IDEA) to run `GrammarCheckerGUI.main()`.

## Usage

1. **Launch the application** - A window will open with a text area
2. **Load a file** - Click "Load File" to open Word (.docx), PDF, or text files
3. **Or paste text** - Directly paste or type text into the text area
4. **Check grammar** - Click "Check Grammar" button
5. **View results** - A popup window will display all grammar issues with:
   - Original problematic text
   - Suggested corrections
   - Explanations for each issue
   - Position in the document

**The original file is never modified** - all suggestions are displayed in a separate popup window.

## How It Works

### Grammar Checking Logic

The application uses OpenAI's GPT-4o-mini model to analyze text:

1. **Text Extraction**: Parses Word/PDF documents to extract plain text
2. **API Call**: Sends text to OpenAI with a specialized prompt requesting grammar analysis
3. **Response Parsing**: Extracts grammar issues from the JSON response
4. **Display**: Shows issues in a user-friendly popup without modifying the source

### Security

- API keys are stored in `.env` file (excluded from Git via `.gitignore`)
- Environment variables are loaded at runtime using `Config.java`
- No hardcoded credentials in source code

### File Support

- **Word Documents (.docx)**: Extracted using Apache POI
- **PDF Documents (.pdf)**: Extracted using Apache PDFBox  
- **Text Files (.txt)**: Read directly
- **Long Text**: Supports documents up to 4000 tokens (configurable in `.env`)

## API Key Security

⚠️ **Never commit your `.env` file to GitHub!**

The `.gitignore` file ensures that `.env` is excluded. Always use `.env.example` as a template.

## Contributing

Feel free to submit issues and enhancement requests!

## License

MIT License - feel free to use and modify as needed.
