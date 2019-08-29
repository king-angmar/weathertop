package xyz.wongs.shumer.akkad.util;

import java.util.UUID;

public class ToolUtils {

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
