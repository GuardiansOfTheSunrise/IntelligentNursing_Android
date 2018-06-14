package com.gots.intelligentnursing.business;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.gots.intelligentnursing.tools.LogUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhqy
 * @date 2018/6/13
 */

public class CrashLogManager {

    private static final String TAG = "CrashLogManager";
    private static final String REGEX_FILE_NAME = "crash_[\\s\\S]*.log";
    private static final String FILE_NAME_PREFIX = "crash_";
    private static final String FILE_NAME_SUFFIX = ".log";

    private String mDirPath;

    private Context mContext;

    private static CrashLogManager sCrashLogManager;

    public static CrashLogManager getInstance(Context context) {
        if (sCrashLogManager == null) {
            synchronized (CrashLogManager.class) {
                if (sCrashLogManager == null) {
                    sCrashLogManager = new CrashLogManager(context);
                }
            }
        }
        return sCrashLogManager;
    }

    private CrashLogManager(Context context) {
        mContext = context.getApplicationContext();
        mDirPath = mContext.getFilesDir().getPath() + "/logs/";
    }

    public List<LogFile> getLogFiles() {
        File dir = new File(mDirPath);
        if (!dir.exists() || dir.isFile()) {
            return null;
        } else {
            String[] files = dir.list((dir1, name) -> {
                Pattern pattern = Pattern.compile(REGEX_FILE_NAME);
                Matcher matcher = pattern.matcher(name);
                return matcher.matches();
            });
            if (files.length == 0) {
                return null;
            }
            List<LogFile> logFiles = new ArrayList<>();
            for (String file : files) {
                logFiles.add(new LogFile(mDirPath, file));
            }
            return logFiles;
        }
    }

    public void log(Throwable e) throws IOException {
        File dir = new File(mDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String datetime = dateFormat.format(new Date(timeStamp));

        String fileName = FILE_NAME_PREFIX + datetime + FILE_NAME_SUFFIX;
        File file = new File(mDirPath + fileName);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        pw.println(datetime);
        try {
            writeEnvironmentInfo(pw);
            pw.println();
            e.printStackTrace(pw);
        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            pw.close();
        }
    }

    private void writeEnvironmentInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print('_');
        pw.println(Build.VERSION.SDK_INT);

        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        pw.print("Model: ");
        pw.println(Build.MODEL);

        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);

        pw.print("User: ");
        if (UserContainer.getUser().getUserInfo() != null) {
            pw.println(UserContainer.getUser().getUserInfo().getUsername());
        } else {
            pw.println("N/A");
        }
    }

    public static class LogFile {

        private static final String ENCODING = "UTF-8";
        private static final String SEPARATOR = "/";

        private File mFile;

        private LogFile(String dir, String name) {
            String path;
            if (dir.endsWith(SEPARATOR)) {
                path = dir + name;
            } else {
                path = dir + SEPARATOR + name;
            }
            mFile = new File(path);
        }

        public String getContent() {
            if (mFile.exists() && mFile.isFile()) {
                Long fileLength = mFile.length();
                byte[] fileContent = new byte[fileLength.intValue()];
                try {
                    FileInputStream in = new FileInputStream(mFile);
                    in.read(fileContent);
                    in.close();
                    return new String(fileContent, ENCODING);
                } catch (UnsupportedEncodingException e) {
                    LogUtil.e(TAG, "Coding error, unable to code the log file from bytes to UTF8.");
                    LogUtil.logExceptionStackTrace(TAG, e);
                } catch (IOException e) {
                    LogUtil.e(TAG, "Unexpected IOException.");
                    LogUtil.logExceptionStackTrace(TAG, e);
                }
            }
            return null;
        }

        public boolean delete() {
            return mFile.exists() && mFile.isFile() && mFile.delete();
        }
    }
}
