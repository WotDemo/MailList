package ysn.com.maillist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import ysn.com.maillist.R;

/**
 * @Author yangsanning
 * @ClassName SideBar
 * @Description 侧边字母bar
 * @Date 2019/2/18
 * @History 2019/2/18 author: description:
 */
public class SideBar extends View {

    private OnLetterChangedListener onLetterChangedListener;

    public final static String[] LETTERS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
    private int choose = -1;
    private Paint paint = new Paint();

    /**
     * 显示字母的TextView
     */
    private TextView dialogTextView;

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        // 获取每一个字母的高度
        int letterHeight = height / LETTERS.length;

        for (int i = 0; i < LETTERS.length; i++) {
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            if (i == choose) {// 选中的状态
                paint.setColor(Color.rgb(255, 255, 255));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半
            float xPos = width / 2 - paint.measureText(LETTERS[i]) / 2;
            float yPos = letterHeight * i + letterHeight;
            canvas.drawText(LETTERS[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数
        final int c = (int) (y / getHeight() * LETTERS.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackground(new ColorDrawable(0x00000000));
                choose = -1;
                invalidate();
                if (dialogTextView != null) {
                    dialogTextView.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setBackgroundResource(R.drawable.bg_side_bar);
                if (oldChoose != c) {
                    if (c >= 0 && c < LETTERS.length) {
                        if (onLetterChangedListener != null) {
                            onLetterChangedListener.onLetter(LETTERS[c]);
                        }
                        if (dialogTextView != null) {
                            dialogTextView.setText(LETTERS[c]);
                            dialogTextView.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setTextView(TextView textDialog) {
        this.dialogTextView = textDialog;
    }

    public void setOnLetterChangeListener(
            OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    public interface OnLetterChangedListener {
        void onLetter(String letter);
    }
}