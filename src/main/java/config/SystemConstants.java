package config;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class SystemConstants {

    // General specifics
    public static final DecimalFormat UTC_DECIMAL_FORMATER = new DecimalFormat("#.##");
    public static final String UTC_DATETIME_FORMATER = "yyyy-MM";

    public static final String UTC_CURRENT_MONTH_BILL_DATE = LocalDate.now().toString(DateTimeFormat.forPattern(SystemConstants.UTC_DATETIME_FORMATER));

    // Database specifics
    public static final String UTC_USERS_TABLE = "utc.users";

    // Bill specifics
    public static final String CURRENCY = "EUR";

    public static final String BILL_DESTINATION_PATH = "src/main/resources/data/";
    public static final String BILL_DESTINATION_TEST_PATH = "src/test/resources/data/";

    public static final int INDICATOR_MIN = 10;
    public static final int INDICATOR_MAX = 96;

    public static final double UTILITY_PRICE_MIN = 0.09d;
    public static final double UTILITY_PRICE_MAX = 0.64d;

    public static final double PVM_MIN = 0.05d;
    public static final double PVM_MAX = 0.15d;

    public static final List<String> UTILITIES = Arrays.asList("heat", "electricity", "water", "gas", "trash");

    public static final String UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES = BILL_DESTINATION_PATH + UTC_CURRENT_MONTH_BILL_DATE + "_UtilityPrices.json";
    public static final String UTC_ROLLING_MONTH_UTILITY_UNIT_PRICES_PVM = BILL_DESTINATION_PATH + UTC_CURRENT_MONTH_BILL_DATE + "_PVM.json";
}
