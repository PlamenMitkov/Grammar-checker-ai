# Quick Start Guide

## Step 1: Set Up Your API Key

```bash
# Copy the example environment file
cp .env.example .env

# Edit .env and add your OpenAI API key
nano .env  # or use any text editor
```

In the `.env` file, replace `your_openai_api_key_here` with your actual API key:
```
OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxxxxx
```

## Step 2: Install Dependencies

Using Maven:
```bash
mvn clean install
```

## Step 3: Run the Application

### Option A: Using Maven
```bash
mvn exec:java -Dexec.mainClass="com.grammarchecker.gui.GrammarCheckerGUI"
```

### Option B: Build and run JAR
```bash
mvn package
java -jar target/grammar-checker-ai-1.0.0-jar-with-dependencies.jar
```

### Option C: Using your IDE
1. Import the project as a Maven project
2. Run `com.grammarchecker.gui.GrammarCheckerGUI` main class

## Step 4: Use the Application

1. **Load a file**: Click "Load File" → Select a .docx, .pdf, or .txt file
2. **Or paste text**: Type or paste text directly into the text area
3. **Check grammar**: Click "Check Grammar" button
4. **View results**: A popup window shows all grammar issues with suggestions

## Example Files to Test

Create a test file with some intentional errors:

**test.txt:**
```
Their is many grammar errors in this sentence. Me and him went to the store yesterday. 
The dog are running in the park. She dont like ice cream.
```

Load this file and click "Check Grammar" to see the AI analyze and suggest corrections!

## Troubleshooting

### "OpenAI API key not configured"
- Make sure you created `.env` file from `.env.example`
- Verify your API key is correct and has credits

### File won't load
- Ensure the file is in a supported format (.docx, .pdf, .txt)
- Check file permissions

### Dependencies missing
- Run `mvn clean install` to download all dependencies
- Make sure you have Java 17+ installed

## Features

✅ Analyzes grammar, spelling, and style
✅ Works with long documents (up to 4000 tokens)
✅ Supports Word, PDF, and text files
✅ Never modifies your original files
✅ Shows detailed explanations for each issue
✅ Secure - API key stored locally in .env (never committed to git)
