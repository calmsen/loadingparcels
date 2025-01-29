package ru.calmsen.loadingparcels.command;

public class CommandParameter {
    public static final String USER = "user";

    public static class FindParcel{
        public static final String NAME = "find";
        public static final String OUT_FORMAT = "out-format";
        public static final String PAGE_NUMBER = "page-number";
        public static final String PAGE_SIZE = "page-size";

        public static final String PAGE_NUMBER_DEFAULT_VALUE = "1";
        public static final String PAGE_SIZE_DEFAULT_VALUE = "10";
        public static final String OUT_FORMAT_DEFAULT_VALUE = "TXT";
    }

    public static class CreateParcel{
        public static final String NAME = "name";
        public static final String FORM = "form";
        public static final String SYMBOL = "symbol";
    }

    public static class UpdateParcel{
        public static final String NAME = "name";
        public static final String FORM = "form";
        public static final String SYMBOL = "symbol";
    }

    public static class DeleteParcel{
        public static final String NAME = "delete";
    }

    public static class LoadParcels{
        public static final String IN_FILE = "load";
        public static final String PARCEL_NAMES = "parcel-names";
        public static final String OUT_FILE = "out-file";
        public static final String OUT_FORMAT = "out-format";
        public static final String LOADING_MODE = "loading-mode";
        public static final String TRUCKS = "trucks";
        public static final String TRUCKS_COUNT = "trucks-count";
        public static final String TRUCKS_WIDTH = "trucks-width";
        public static final String TRUCKS_HEIGHT = "trucks-height";

        public static final String LOADING_MODE_DEFAULT_VALUE = "ONEPARCEL";
        public static final String OUT_FORMAT_DEFAULT_VALUE = "TXT";

        public static final String TRUCKS_WIDTH_DEFAULT_VALUE = "6";
        public static final String TRUCKS_HEIGHT_DEFAULT_VALUE = "6";
        public static final String TRUCKS_COUNT_DEFAULT_VALUE = "50";
    }

    public static class UnloadParcels{
        public static final String IN_FILE = "unload";
        public static final String OUT_FORMAT = "out-format";
        public static final String WITH_COUNT = "with-count";

        public static final String WITH_COUNT_DEFAULT_VALUE = "false";
        public static final String OUT_FORMAT_DEFAULT_VALUE = "TXT";
    }

    public static class Billing{
        public static final String FROM = "from";
        public static final String TO = "to";
        public static final String OUT_FORMAT = "out-format";

        public static final String OUT_FORMAT_DEFAULT_VALUE = "TXT";
    }
}
