package com.butler.smartbutler.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.butler.smartbutler.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoUtil {
    //默认加载图片
    public static void loadImageView(String imgUrl, ImageView imageView) {
        Picasso.get().load(imgUrl).into(imageView);
    }

    //默认加载图片 指定大小
    public static void loadImageViewSize(String imageUrl, ImageView imageView, int width, int height) {
        Picasso.get()
                .load(imageUrl)
                .resize(width, height)
                .centerCrop()
                .into(imageView);
    }

    public static void loadImageViewSize(String imageUrl, ImageView imageView) {
        loadImageViewSize(imageUrl, imageView, 220, 120);
    }

    //带默认图片
    public static void loadImageViewHolder(String imageUrl, ImageView imageView, int loadImage, int errorImage) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(loadImage)
                .error(errorImage)
                .into(imageView);
    }

    public static void loadImageViewHolder(String imageUrl, ImageView imageView) {
        loadImageViewHolder(imageUrl, imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    //裁剪图片
    public static void loadImageViewCrop(String imageUrl,ImageView imageView){
        Picasso.get().load(imageUrl).transform(new CropSquareTransformation()).into(imageView);
    }

    //按比例裁剪
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "square()";
        }
    }
}
