package com.pay.library.uils;

import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import com.pay.library.config.AppConfig;


public final class Logger {

    private static final String APP_TAG = "TDPay";

    public static final boolean DBG = AppConfig.DEBUG;

    private static final String LOG_FILE_NAME = "CGPay.txt";

    private static PrintStream logStream;

    private static final String LOG_ENTRY_FORMAT = "[%tF %tT][%s]%s";

    private Logger() {

    }

    private static String formatMsg(String tag, String msg) {
        return tag + " - " + msg;
    }

    public static void e(String tag, String msg) {
        Log.e(APP_TAG, formatMsg(tag, msg));
        write(APP_TAG, formatMsg(tag, msg), null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(APP_TAG, formatMsg(tag, msg), tr);
        write(APP_TAG, formatMsg(tag, msg), tr);
    }

    public static void w(String tag, String msg) {
        Log.w(APP_TAG, formatMsg(tag, msg));
        write(APP_TAG, formatMsg(tag, msg), null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        Log.w(APP_TAG, formatMsg(tag, msg), tr);
        write(APP_TAG, formatMsg(tag, msg), tr);
    }

    public static void i(String tag, String msg) {
        Log.i(APP_TAG, formatMsg(tag, msg));
        write(APP_TAG, formatMsg(tag, msg), null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        Log.i(APP_TAG, formatMsg(tag, msg), tr);
        write(APP_TAG, formatMsg(tag, msg), tr);
    }

    public static void d(String tag, String msg) {
        if (Logger.DBG) {
            Log.d(APP_TAG, formatMsg(tag, msg));
            write(APP_TAG, formatMsg(tag, msg), null);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (Logger.DBG) {
            Log.d(APP_TAG, formatMsg(tag, msg), tr);
            write(APP_TAG, formatMsg(tag, msg), tr);
        }
    }

    public static void v(String tag, String msg) {
        Log.v(APP_TAG, formatMsg(tag, msg));
        write(APP_TAG, formatMsg(tag, msg), null);
    }

    public static void v(String tag, String msg, Throwable tr) {
        Log.v(APP_TAG, formatMsg(tag, msg), tr);
        write(APP_TAG, formatMsg(tag, msg), tr);
    }

    private synchronized static void write(String tag, String msg, Throwable tr) {
        if (!Logger.DBG)
            return;
        try {

            if (null == logStream) {
                synchronized (Logger.class) {
                    if (null == logStream) {
                        init();
                    }
                }
            }

            Date now = new Date();
            if (null != logStream) {
                logStream.printf(LOG_ENTRY_FORMAT, now, now, tag, msg);
                logStream.print("\n");
                // logStream.println();
            }
            if (null != tr) {
                tr.printStackTrace(logStream);
                if (null != logStream) {
                    logStream.print("\n");
                    // logStream.println("\n");
                }
            }
        } catch (Throwable t) {
        }
    }

    public static void init() {
        if (!Logger.DBG)
            return;
        try {
            File sdRoot = getSDRootFile();
            if (sdRoot != null) {
                File logFile = new File(sdRoot, LOG_FILE_NAME);

                // Log.d(APP_TAG, formatMsg(TAG, " : Log to file : " +
                // logFile));
                logStream = new PrintStream(
                        new FileOutputStream(logFile, true), true);
            }
        } catch (Throwable e) {
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
            if (logStream != null)
                logStream.close();
        } catch (Throwable t) {
        }
    }

    /**
     * Get the SD card's root file.
     *
     * @return the SD card's root file.
     */
    public static File getSDRootFile() {
        if (isSdCardReady()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    private static final boolean isSdCardReady() {
        String cardstatus = Environment.getExternalStorageState();
        if (cardstatus.equals(Environment.MEDIA_REMOVED)
                || cardstatus.equals(Environment.MEDIA_UNMOUNTABLE)
                || cardstatus.equals(Environment.MEDIA_UNMOUNTED)
                || cardstatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return false;
        }

        return true;
    }

}
