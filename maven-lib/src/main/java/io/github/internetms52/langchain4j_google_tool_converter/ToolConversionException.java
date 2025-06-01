package io.github.internetms52.langchain4j_google_tool_converter;

public class ToolConversionException extends RuntimeException {

    public ToolConversionException(String message) {
        super(message);
    }

    public ToolConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
