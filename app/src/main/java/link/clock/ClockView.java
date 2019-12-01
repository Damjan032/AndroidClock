package link.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

public class ClockView extends View {
    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int radius = 0;
    private int handTruncation, hourHandTruncation = 0;
    private Paint paint;
    private boolean isInit;
    private int[] numbers = {1,2,3,4,5,6,7,8,9,10,11,12};
    private Rect rect = new Rect();
    int min ;
    int sec ;
    int hour;
    boolean isChangedSec = false;
    boolean isChangedType = false;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initClock(){
        height = getHeight();
        width = getWidth();
        padding = 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,13, getResources().getDisplayMetrics());
        int min = Math.min(height,width);

        radius = min / 2 - padding;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInit)
            initClock();

        drawClock(canvas);
    }

    private void drawClock(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        drawCircle(canvas);
        drawCenter(canvas);
        drawNumbers(canvas);
        drawLines(canvas);
        postInvalidateDelayed(1000);
        invalidate();
    }

    private void drawCircle(Canvas canvas){
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        canvas.drawCircle(width/2,height/2,radius+padding-10,paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
    }

    private void drawCenter(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width/2, height/2, 12, paint);
    }

    private void drawNumbers(Canvas canvas){

        paint.setTextSize(fontSize);
        for (int num : numbers){
            String strNum = String.valueOf(num);
            paint.getTextBounds(strNum, 0, strNum.length(), rect);

            double angle = Math.PI / 6 * (num-3);
            int x = (int) (width/2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height/2 + Math.sin(angle) * radius - rect.height() / 2);

            canvas.drawText(strNum, x, y+10, paint);
        }
    }

    private void drawLines(Canvas canvas, double loc, boolean isHour, boolean isMin){
        paint.setColor(Color.WHITE);
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = radius - handTruncation;
        if(isHour) {
            paint.setColor(Color.CYAN);
            handRadius = radius - handTruncation - hourHandTruncation;
        }
        if(isMin) {
            paint.setColor(Color.GRAY);
            handRadius = radius - handTruncation - hourHandTruncation + 20;
        }
        canvas.drawLine(width/2, height/2,
                (float)(width /2 + Math.cos(angle) * handRadius),
                (float)(height/2 + Math.sin(angle) * handRadius),
                paint);
    }

    private void drawLines(Canvas canvas){
        if(MainActivity.isAuto){
            drawHandsAuto(canvas);
            isChangedType=false;
        }
        else {
            if(!isChangedType){
            min = MainActivity.minuts;
            hour = MainActivity.hours;
            sec = 35;
            isChangedType = true;
            }
            drawHandsManual(canvas);
        }
    }

    private void drawHandsAuto(Canvas canvas){
        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        min= calendar.get(Calendar.MINUTE);
        sec = calendar.get(Calendar.SECOND);
        drawLines(canvas, (hour + min /60.0) * 5f, true, false);
        drawLines(canvas, min, false, true);
        drawLines(canvas, sec, false, false);
    }

    private void drawHandsManual(Canvas canvas){
        Calendar calendar = Calendar.getInstance();
        drawLines(canvas, (hour + min /60.0) * 5f, true, false);
        drawLines(canvas, min, false, true);
        drawLines(canvas, sec, false, false);
        sec = calendar.get(Calendar.SECOND);
        if (sec == 0 && !isChangedSec) {
            min++;
            if (min == 60) {
                min = 0;
                hour++;
                if (hour == 12) {
                    hour = 0;
                }
            }
            isChangedSec=true;
        }
        if(sec==1){
            isChangedSec=false;
        }
    }
}
