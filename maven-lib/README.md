# LangChain4j Google Tool Converter

Convert LangChain4j tool specifications to Google GenAI tool format.

## Quick Start

```xml

<dependency>
    <groupId>io.github.internetms52</groupId>
    <artifactId>langchain4j-google-tool-converter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Common Use Case
```java
// Get tools from MCP client
List<ToolSpecification> mcpTools = mcpClient.listTools();

// Convert for Google GenAI
List<Tool> googleTools = LangChain4jGoogleToolConverter.convertSafely(mcpTools);

// Use with Google GenAI chat model
chatModel.chat(prompt, googleTools);
```

## Usage Example

```java
// Static usage (simple)
Tool googleTool=LangChain4jGoogleToolConverter.convert(langchainTool);

// Batch conversion with error handling
List<Tool> googleTools=LangChain4jGoogleToolConverter.convertSafely(langchainTools);
```

## API Reference

**Static Methods:**
- `convert(ToolSpecification)` - Convert single tool, throws exception on failure
- `convert(List<ToolSpecification>)` - Convert multiple tools, stops on first failure

**Instance Methods:**
- `createToolFromLangChainTool(ToolSpecification)` - Convert single tool
- `createToolsFromLangChainTools(List<ToolSpecification>)` - Convert multiple tools
- `createToolsFromLangChainToolsSafely(List<ToolSpecification>)` - Convert with error tolerance, skips failed conversions

# License
MIT

