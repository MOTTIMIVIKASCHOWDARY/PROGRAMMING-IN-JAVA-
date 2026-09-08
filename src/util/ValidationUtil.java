package util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isPositive(int number) {
        return number > 0;
    }

    public static boolean isNonNegative(int number) {
        return number >= 0;
    }

    public static boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isValidPrice(String priceStr) {
        if (!isNotEmpty(priceStr)) return false;
        try {
            BigDecimal price = new BigDecimal(priceStr.trim());
            return price.compareTo(BigDecimal.ZERO) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static int parsePositiveInt(String val, int defaultValue) {
        if (!isNotEmpty(val)) return defaultValue;
        try {
            int num = Integer.parseInt(val.trim());
            return num > 0 ? num : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
