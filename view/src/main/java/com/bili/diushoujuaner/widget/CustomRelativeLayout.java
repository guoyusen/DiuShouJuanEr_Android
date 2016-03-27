package com.bili.diushoujuaner.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bili.diushoujuaner.R;
import com.bili.diushoujuaner.utils.Common;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by BiLi on 2016/3/20.
 */
public class CustomRelativeLayout extends RelativeLayout {

    private Bitmap drawBitmap;
    private float drawWidth, drawHeight;
    private Context context;
    private ImageRequest imageRequest = null;
    private DataSource<CloseableReference<CloseableImage>> dataSource = null;
    private String bgUrl;

    public CustomRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        if(isInEditMode()){
            return;
        }
        this.context = context;
        drawHeight = getResources().getDimension(R.dimen.y500);
        showTopBg();
        setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.COLOR_WHITE)));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        drawWidth = measureWidth(widthMeasureSpec);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getPaddingLeft() + getPaddingRight() + getWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);// 60,480
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.drawBitmap != null){
            drawImage(canvas, this.drawBitmap, 0, 0, (int)drawWidth, (int)(drawWidth >= drawHeight ? drawWidth : drawHeight), 0, 0);
        }
    }

    private void showTopBg(){
        if(Common.isEmpty(this.bgUrl)){
            imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.mipmap.bg_menu_head)
                    .setProgressiveRenderingEnabled(true)
                    .build();
        }else{
            imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(Common.getCompleteUrl(this.bgUrl)))
                    .setProgressiveRenderingEnabled(true)
                    .build();
        }
        dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, this.context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                drawBitmap = bitmap;
                invalidate();
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
            }
        }, CallerThreadExecutor.getInstance());
    }

    public void setBgUrl(String url){
        this.bgUrl = url;
        showTopBg();
    }

    public void drawImage(Canvas canvas, Bitmap blt, int x, int y,
                          int w, int h, int bx, int by) {
        Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形

        src.left = bx;
        src.top = by;
        src.right = bx + w;
        src.bottom = by + h;

        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        canvas.drawBitmap(blt, null, dst, null);
    }

}
