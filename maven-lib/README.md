# LangChain4j Google Tool Converter

Convert LangChain4j tool specifications to Google GenAI tool format.

## Quick Start

```xml
<dependency>
    <groupId>io.github.internetms52</groupId>
    <artifactId>langchain4j-google-tool-converter</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Usage Example

```java
// Static usage (simple)
Tool googleTool = LangChain4jGoogleToolConverter.convert(langchainTool);

// Batch conversion with error handling
List<Tool> googleTools = LangChain4jGoogleToolConverter.convertSafely(langchainTools);
```

## API Reference
- convert(ToolSpecification) - Convert single tool
- convert(List<ToolSpecification>) - Convert multiple tools
- convertSafely(List<ToolSpecification>) - Convert with error tolerance

# License
MIT

