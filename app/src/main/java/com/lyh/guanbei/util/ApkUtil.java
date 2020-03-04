package com.lyh.guanbei.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import androidx.core.content.FileProvider;
import okhttp3.ResponseBody;

public class ApkUtil {
    //安装
    public static Intent getInstallApkIntent(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //第二个参数需要与<provider>标签中的android:authorities属性相同
            uri = FileProvider.getUriForFile(context, "com.lyh.guanbei.fileProvider", file);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    //得到apk
    public static File getApkFile(Context context, String name) {
        try {
            File apk = new File(context.getExternalFilesDir("apk"), name);
            if (apk.getParentFile().exists())
                apk.getParentFile().mkdirs();
            if (!apk.exists())
                apk.createNewFile();
            return apk;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkApkIsDownloadDone(File file, long size) {
        return file.length() == size;
    }

    public static boolean checkIsNeedNewVersion(Context context, String newVersion) {
        try {
            PackageManager manager = context.getPackageManager();
            String nowVersion = manager.getPackageInfo(context.getPackageName(), 0).versionName;
            LogUtil.logD("nowVersion "+nowVersion);
            return newVersion.compareTo(nowVersion) > 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean downloadApkFromStream(InputStream inputStream, File apk) {
        try {
            LogUtil.logD("download  ");
            // todo change the file location/name according to your needs
            BufferedInputStream bufferedInputStream = null;
            RandomAccessFile outFile = null;
            try {
                byte[] fileReader = new byte[1024 * 1024];
                long fileSizeDownloaded = apk.length();
                bufferedInputStream = new BufferedInputStream(inputStream);
                outFile = new RandomAccessFile(apk, "rw");
                outFile.seek(fileSizeDownloaded);
                LogUtil.logD("开始下载 "+fileSizeDownloaded);
                int len;
                while ((len = bufferedInputStream.read(fileReader)) != -1) {
                    outFile.write(fileReader, 0, len);
                    LogUtil.logD("len  " + len + "   已下载 " + fileSizeDownloaded);
                    fileSizeDownloaded += len;
                }
                LogUtil.logD("下载完成");
                return true;
            } catch (IOException e) {
                LogUtil.logD("出错 "+e.getMessage());
                return false;
            } finally {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }

                if (outFile != null) {
                    outFile.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
