package com.jcs.where.utils;

import static android.os.Environment.DIRECTORY_PICTURES;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by Wangsw  2022/4/1 14:57.
 */
public class FileUtil {


    public static String getImagesPath(Context context) {



        final String path = context.getExternalFilesDir(DIRECTORY_PICTURES).getPath() + "/where/compression/";

        return createPathIfNotExist(path,context);
    }

    public static String createPathIfNotExist(String pPath, Context context) {
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if (!sdExist) {
            LogUtils.e("path", "SD卡不存在");
            return null;
        }

        File dirFile = new File(pPath);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                LogUtils.e("path", "文件夹创建失败");
                return null;
            }
        }
        return pPath;
    }

   /* private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/thumbnail";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }*/
}
