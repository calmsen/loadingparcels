package ru.calmsen.billing.command;

public class CommandParameter {
    public static final String USER = "user";

    public static class Billing{
        public static final String FROM = "from";
        public static final String TO = "to";
        public static final String PERIOD = "period";
        public static final String OUT_FORMAT = "out-format";

        public static final String PERIOD_DEFAULT_VALUE = "NONE";
        public static final String OUT_FORMAT_DEFAULT_VALUE = "TXT";
    }
}
