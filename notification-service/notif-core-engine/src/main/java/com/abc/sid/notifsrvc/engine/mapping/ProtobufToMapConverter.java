package com.abc.sid.notifsrvc.engine.mapping;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a typed Protobuf Message directly into an in-memory Map keyed by proto field names,
 * eliminating intermediate JSON string serialization/deserialization overhead.
 */
public final class ProtobufToMapConverter {

    private ProtobufToMapConverter() {}

    public static Map<String, Object> toMap(Message message) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (message == null) {
            return result;
        }
        for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : message.getAllFields().entrySet()) {
            Descriptors.FieldDescriptor field = entry.getKey();
            Object value = entry.getValue();
            result.put(field.getName(), convertValue(field, value));
        }
        return result;
    }

    private static Object convertValue(Descriptors.FieldDescriptor field, Object value) {
        if (field.isRepeated() && value instanceof List<?> list) {
            List<Object> convertedList = new ArrayList<>(list.size());
            for (Object item : list) {
                convertedList.add(convertSingleValue(field, item));
            }
            return convertedList;
        }
        return convertSingleValue(field, value);
    }

    private static Object convertSingleValue(Descriptors.FieldDescriptor field, Object value) {
        return switch (field.getJavaType()) {
            case MESSAGE -> toMap((Message) value);
            case ENUM -> ((Descriptors.EnumValueDescriptor) value).getName();
            default -> value;
        };
    }
}
