package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.usmart.com.moda.R;

public class ImagesUtils {

    public static Bitmap getWidgetBitmap(Context context, String Text, int percentage, String bgColorHex, String roundColorHex) {

        int width = 200;
        int height = 200;
        int stroke = 15;
        int padding = 5;
        float density = context.getResources().getDisplayMetrics().density;

        //Paint for arc stroke.
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(stroke);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //paint.setStrokeJoin(Paint.Join.ROUND);
        //paint.setPathEffect(new CornerPathEffect(10) );

        //Paint for text values.
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize((int) (context.getResources().getDimension(R.dimen.widget_text_large_value) / density));
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        final RectF arc = new RectF();
        arc.set((stroke / 2) + padding, (stroke / 2) + padding, width - padding - (stroke / 2), height - padding - (stroke / 2));

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //First draw full arc as background.
        //   paint.setColor(Color.argb(75, 255, 255, 255));
        paint.setColor(Color.parseColor("#" + bgColorHex));
        canvas.drawArc(arc, -90, 275, false, paint);
        //Then draw arc progress with actual value.
        paint.setColor(Color.parseColor("#" + roundColorHex));
        float x = (float) percentage / 100;
        float xx = (x * 275);
        float xxx = Math.round(x * 275);

        Log.i("TestApp_1", x + "");
        Log.i("TestApp_2", xx + "");
        Log.i("TestApp_3", xxx + "");
        canvas.drawArc(arc, -90, xxx, false, paint);
        //Draw text value.
      //  canvas.drawText(percentage + "%", bitmap.getWidth() / 2, (bitmap.getHeight() - mTextPaint.ascent()) / 2, mTextPaint);
        canvas.drawText(Text, bitmap.getWidth() / 2, (bitmap.getHeight() - mTextPaint.ascent()) / 2, mTextPaint);
        //Draw widget title.
        mTextPaint.setTextSize((int) (context.getResources().getDimension(R.dimen.widget_text_large_title) / density));
      //  canvas.drawText(context.getString(R.string.widget_text_arc_battery), bitmap.getWidth() / 2, bitmap.getHeight() - (stroke + padding), mTextPaint);

        return bitmap;
    }
}
