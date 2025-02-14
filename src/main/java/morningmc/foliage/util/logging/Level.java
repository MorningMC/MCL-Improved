package morningmc.foliage.util.logging;

public enum Level {
    TRACE, DEBUG, INFO, WARN, ERROR, FATAL;

    public boolean isError() {
        return this == ERROR || this == FATAL;
    }
}
