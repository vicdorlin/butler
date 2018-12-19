package com.butler.smartbutler.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImageUtils {
    public static void saveBitmap(View view, String filePath){

        // 创建对应大小的bitmap
        Bitmap  bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        //存储
        FileOutputStream outStream = null;
        File file=new File(filePath);
        if(file.isDirectory()){//如果是目录不允许保存
            return;
        }
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bitmap.recycle();
                if(outStream!=null){
                    outStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
