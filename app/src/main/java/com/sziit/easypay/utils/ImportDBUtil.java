package com.sziit.easypay.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.sziit.easypay.pub.Pub.EASY_SETTLEMENT_PATH;

/**
 * Created by inanitysloth on 2017/1/10.
 */

public class ImportDBUtil {
    private String strClassName = "ImportDBUtil";
    private SQLiteDatabase mSqliteDatebase;

    public SQLiteDatabase openDatabase(Context mCtx, String strFilePath, String strDirPath) {
        File mFilePath = new File(strFilePath);
        // 查看数据库文件是否存在
        if (mFilePath.exists()) {
            Log.d(strClassName, "strFilePath");
            //  存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(mFilePath, null);

        } else {
            // 不存在先创建文件夹
            File mFileDir = new File(strDirPath);
            Log.d("strClassName", "pathStr=" + mFileDir);
            if (mFileDir.mkdir()) {
                Log.d("strClassName", "创建成功");
            } else {
                Log.d("strClassName", "创建失败");
            }
            try {
                // 得到资源
                AssetManager am = mCtx.getAssets();
                // 得到数据库的输入流
                InputStream is = am.open(EASY_SETTLEMENT_PATH);
                Log.i("test", is + "");
                // 用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(mFilePath);
                Log.d("test", "fos=" + fos);
                Log.d("test", "jhPath=" + mFilePath);
                // 创建byte数组 用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    Log.i("test", "得到");
                    fos.write(buffer, 0, count);
                }
                // 最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            // 如果没有这个数据库 我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(mCtx, strFilePath, strDirPath);
        }
    }
}



