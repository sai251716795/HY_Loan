/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ocr.ui.util;

import android.content.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    public static File getSaveFile(Context context) {
//        SimpleDateFormat timeStampFormat = new SimpleDateFormat("HHmmss");
//        String filename = timeStampFormat.format(new Date());
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }
}
