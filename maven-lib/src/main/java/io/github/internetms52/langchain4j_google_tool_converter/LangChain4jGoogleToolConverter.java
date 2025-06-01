package io.github.internetms52.langchain4j_google_tool_converter;

import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Tool;
import com.google.genai.types.Type;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LangChain4jGoogleToolConverter {
    private static final Logger logger = LoggerFactory.getLogger(LangChain4jGoogleToolConverter.class);

    public static Tool convert(ToolSpecification tool) throws ToolConversionException {
        return new LangChain4jGoogleToolConverter().createToolFromLangChainTool(tool);
    }

    public static List<Tool> convert(List<ToolSpecification> tools) throws ToolConversionException {
        return new LangChain4jGoogleToolConverter().createToolsFromLangChainTools(tools);
    }

    public List<Tool> createToolsFromLangChainTools(List<ToolSpecification> tools) throws ToolConversionException {
        return tools.stream()
                .map(this::createToolFromLangChainTool)
                .toList();
    }

    public List<Tool> createToolsFromLangChainToolsSafely(List<ToolSpecification> tools) {
        return tools.stream()
                .map(tool -> {
                    try {
                        return createToolFromLangChainTool(tool);
                    } catch (ToolConversionException e) {
                        logger.warn("Failed to convert tool: {}, skipping...", tool.name(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public Tool createToolFromLangChainTool(ToolSpecification mcpTool) throws ToolConversionException {
        if (mcpTool == null) {
            throw new ToolConversionException("ToolSpecification cannot be null");
        }
        try {
            FunctionDeclaration functionDecl = FunctionDeclaration.builder()
                    .name(mcpTool.name())
                    .description(mcpTool.description())
                    .parameters(convertJsonObjectSchema(mcpTool.parameters()))
                    .build();

            return Tool.builder()
                    .functionDeclarations(List.of(functionDecl))
                    .build();
        } catch (Exception ex) {
            throw new ToolConversionException(
                    "Failed to convert tool: " + mcpTool.name(), ex);
        }
    }

    private Schema convertJsonObjectSchema(JsonObjectSchema jsonObjectSchema) {
        Schema.Builder schemaBuilder = Schema.builder()
                .type(Type.Known.OBJECT);

        // 設定描述
        if (jsonObjectSchema.description() != null) {
            schemaBuilder.description(jsonObjectSchema.description());
        }

        // 轉換 properties - 這裡會遞迴呼叫 convertPropertySchema
        if (jsonObjectSchema.properties() != null) {
            Map<String, Schema> propertiesMap = new HashMap<>();

            jsonObjectSchema.properties().forEach((propertyName, propertySchema) -> {
                Schema convertedProperty = convertPropertySchema(propertySchema);
                propertiesMap.put(propertyName, convertedProperty);
            });

            schemaBuilder.properties(propertiesMap);
        }

        // 處理 required 欄位
        if (jsonObjectSchema.required() != null) {
            schemaBuilder.required(new ArrayList<>(jsonObjectSchema.required()));
        }

        return schemaBuilder.build();
    }

    private Schema.Builder createSchemaBuilder(Type.Known type, String description) {
        Schema.Builder builder = Schema.builder().type(type);
        if (description != null && !description.trim().isEmpty()) {
            builder.description(description);
        }
        return builder;
    }

    private Schema convertPropertySchema(JsonSchemaElement jsonSchemaElement) {

        if (jsonSchemaElement instanceof JsonStringSchema jsonStringSchema) {
            return createSchemaBuilder(Type.Known.STRING, jsonStringSchema.description()).build();
        } else if (jsonSchemaElement instanceof JsonNumberSchema jsonNumberSchema) {
            return createSchemaBuilder(Type.Known.NUMBER, jsonNumberSchema.description()).build();
        } else if (jsonSchemaElement instanceof JsonIntegerSchema jsonIntegerSchema) {
            return createSchemaBuilder(Type.Known.INTEGER, jsonIntegerSchema.description()).build();
        } else if (jsonSchemaElement instanceof JsonBooleanSchema jsonBooleanSchema) {
            return createSchemaBuilder(Type.Known.BOOLEAN, jsonBooleanSchema.description()).build();
        } else if (jsonSchemaElement instanceof JsonArraySchema jsonArraySchema) {
            Schema.Builder builder = createSchemaBuilder(Type.Known.ARRAY, jsonArraySchema.description());

            // 處理 array items
            if (jsonArraySchema.items() != null) {
                builder.items(convertPropertySchema(jsonArraySchema.items()));
            }
            return builder.build();

        } else if (jsonSchemaElement instanceof JsonObjectSchema jsonObjectSchema) {
            // 遞迴處理嵌套 object
            return convertJsonObjectSchema(jsonObjectSchema);

        } else {
            // 預設 fallback
            return Schema.builder()
                    .type(Type.Known.STRING)
                    .build();
        }
    }
}
