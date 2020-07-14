package com.jcs.where.utils;

import com.jcs.where.Const;

public class ImageUtils {

    public static boolean isLocalPath(String url) {
        if (url.startsWith(Const.IMAGE_ROOT_PATH)) {
            return false;
        }
        return true;
    }

}
