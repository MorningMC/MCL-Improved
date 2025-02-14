package morningmc.foliage.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDate {
    private static final String standardFormat = "yyyy-MM-dd 'at' HH:mm:ss:SSS z";
    private static final String filenameFormat = "yyyy_MM_dd.HH_mm_ss";

    public String format(String format) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public String standardFormat() {
        return format(standardFormat);
    }

    public String filenameFormat() {
        return format(filenameFormat);
    }
}
