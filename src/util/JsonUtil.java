package util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof Timestamp) {
            return "\"" + DATE_FORMAT.format((Timestamp) obj) + "\"";
        }
        if (obj instanceof Date) {
            return "\"" + obj.toString() + "\"";
        }
        if (obj instanceof java.util.Date) {
            return "\"" + DATE_FORMAT.format((java.util.Date) obj) + "\"";
        }
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) sb.append(",");
                sb.append("\"").append(escapeJson(String.valueOf(entry.getKey()))).append("\":");
                sb.append(toJson(entry.getValue()));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
        if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : col) {
                if (!first) sb.append(",");
                sb.append(toJson(item));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj.getClass().isArray()) {
            Object[] array = (Object[]) obj;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < array.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(toJson(array[i]));
            }
            sb.append("]");
            return sb.toString();
        }

        // POJO object reflection
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && method.getName().length() > 3 &&
                method.getParameterCount() == 0 && !method.getName().equals("getClass")) {
                String propName = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);
                try {
                    Object val = method.invoke(obj);
                    if (!first) sb.append(",");
                    sb.append("\"").append(propName).append("\":").append(toJson(val));
                    first = false;
                } catch (Exception ignored) {}
            } else if (method.getName().startsWith("is") && method.getName().length() > 2 &&
                       method.getParameterCount() == 0 && (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                String propName = Character.toLowerCase(method.getName().charAt(2)) + method.getName().substring(3);
                try {
                    Object val = method.invoke(obj);
                    if (!first) sb.append(",");
                    sb.append("\"").append(propName).append("\":").append(toJson(val));
                    first = false;
                } catch (Exception ignored) {}
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < ' ') {
                        String hex = Integer.toHexString(c);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - hex.length(); k++) sb.append('0');
                        sb.append(hex);
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /**
     * Parses a simple flat/semi-flat JSON object string into a Map of key-value pairs.
     */
    public static Map<String, Object> parseJsonObject(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return map;
        }
        String s = jsonStr.trim();
        if (s.startsWith("{")) s = s.substring(1);
        if (s.endsWith("}")) s = s.substring(0, s.length() - 1);
        s = s.trim();
        if (s.isEmpty()) return map;

        int len = s.length();
        int i = 0;
        while (i < len) {
            // Find key start
            while (i < len && (Character.isWhitespace(s.charAt(i)) || s.charAt(i) == ',')) i++;
            if (i >= len) break;
            
            String key = "";
            if (s.charAt(i) == '"') {
                i++;
                int keyStart = i;
                while (i < len && (s.charAt(i) != '"' || s.charAt(i - 1) == '\\')) i++;
                key = s.substring(keyStart, i);
                if (i < len) i++; // skip closing quote
            } else {
                int keyStart = i;
                while (i < len && s.charAt(i) != ':' && !Character.isWhitespace(s.charAt(i))) i++;
                key = s.substring(keyStart, i);
            }

            // Find colon
            while (i < len && s.charAt(i) != ':') i++;
            if (i < len && s.charAt(i) == ':') i++;

            // Skip whitespace
            while (i < len && Character.isWhitespace(s.charAt(i))) i++;
            if (i >= len) break;

            // Parse value
            Object value = null;
            if (s.charAt(i) == '"') {
                i++;
                int valStart = i;
                StringBuilder valSb = new StringBuilder();
                while (i < len) {
                    if (s.charAt(i) == '\\' && i + 1 < len) {
                        char next = s.charAt(i + 1);
                        if (next == '"' || next == '\\' || next == '/') valSb.append(next);
                        else if (next == 'n') valSb.append('\n');
                        else if (next == 'r') valSb.append('\r');
                        else if (next == 't') valSb.append('\t');
                        else valSb.append(next);
                        i += 2;
                    } else if (s.charAt(i) == '"') {
                        break;
                    } else {
                        valSb.append(s.charAt(i));
                        i++;
                    }
                }
                value = valSb.toString();
                if (i < len) i++; // skip closing quote
            } else if (s.charAt(i) == '{' || s.charAt(i) == '[') {
                char open = s.charAt(i);
                char close = open == '{' ? '}' : ']';
                int depth = 0;
                int valStart = i;
                while (i < len) {
                    if (s.charAt(i) == open) depth++;
                    else if (s.charAt(i) == close) {
                        depth--;
                        if (depth == 0) {
                            i++;
                            break;
                        }
                    }
                    i++;
                }
                value = s.substring(valStart, i);
            } else {
                int valStart = i;
                while (i < len && s.charAt(i) != ',' && s.charAt(i) != '}') i++;
                String raw = s.substring(valStart, i).trim();
                if ("true".equalsIgnoreCase(raw)) value = Boolean.TRUE;
                else if ("false".equalsIgnoreCase(raw)) value = Boolean.FALSE;
                else if ("null".equalsIgnoreCase(raw)) value = null;
                else {
                    try {
                        if (raw.contains(".")) {
                            value = Double.parseDouble(raw);
                        } else {
                            value = Long.parseLong(raw);
                        }
                    } catch (Exception e) {
                        value = raw;
                    }
                }
            }

            map.put(key, value);
        }

        return map;
    }

    public static String getString(Map<String, Object> map, String key, String defaultVal) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) return defaultVal;
        return String.valueOf(map.get(key));
    }

    public static int getInt(Map<String, Object> map, String key, int defaultVal) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) return defaultVal;
        Object v = map.get(key);
        if (v instanceof Number) return ((Number) v).intValue();
        try {
            return Integer.parseInt(String.valueOf(v).trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static BigDecimal getBigDecimal(Map<String, Object> map, String key, BigDecimal defaultVal) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) return defaultVal;
        Object v = map.get(key);
        if (v instanceof Number) return BigDecimal.valueOf(((Number) v).doubleValue());
        try {
            return new BigDecimal(String.valueOf(v).trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
