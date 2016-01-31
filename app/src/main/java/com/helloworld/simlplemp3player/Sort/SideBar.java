package com.helloworld.simlplemp3player.Sort;

/**
 * Created by Administrator on 2016/1/30 0030.
 *
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.R;

public class SideBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private TextView mTextDialog;

    /**
     * 为SideBar设置显示字母的TextView
     *
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }


    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / OtherData.FIRST_CHAR.length;// 获取每一个字母的高度

        for (int i = 0; i < OtherData.FIRST_CHAR.length; i++) {
            paint.setColor(getResources().getColor(R.color.gray));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(28);
            // 选中的状态
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.black));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(OtherData.FIRST_CHAR[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(OtherData.FIRST_CHAR[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * OtherData.FIRST_CHAR.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.//个数？位置吧

        switch (action) {
            case MotionEvent.ACTION_UP://public static final int ACTION_UP = 1;单点触摸离开动作        ，取消所有点击效果
                setBackgroundColor(getResources().getColor(R.color.background_gray));//设置为透明？还是有点背景吧
                choose = -1;//手指已经抬起
                invalidate();
                if (mTextDialog != null) {//使中间的TextView消失
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default://其他动作，将List/RecyclerView里面的数据移动到合适的地方,刷新//后来发现貌似是按下过后的动作
                setBackgroundResource(R.color.gray);
                if (oldChoose != c) {
                    if (c >= 0 && c < OtherData.FIRST_CHAR.length) {//判断是否出界？0.0
                        if (listener != null) {
                            listener.onTouchingLetterChanged(OtherData.FIRST_CHAR[c].charAt(0));//将对应字母传进去，应该是回调，改变TextView的字母，并执行索引操作
                        }
                        if (mTextDialog != null) {//妈蛋，TextView是在这儿设置，上面就应该是索引操作了
                            mTextDialog.setText(OtherData.FIRST_CHAR[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {//回调
        public void onTouchingLetterChanged(char ch);
    }
}