package com.hx.view.widget;


import com.example.hx_view.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
/**
 * 带清除按钮的EditText
 * @author Ding
 *
 */
public class EditTextWithClear extends EditText {
	private Drawable drawright;
	private boolean focused = false;

	public EditTextWithClear(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public EditTextWithClear(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public EditTextWithClear(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EditTextWithClear(Context context) {
		super(context);
		init();
	}

	private void init() {
		Drawable[] draw = this.getCompoundDrawables();
		drawright = getResources().getDrawable(R.drawable.download_cancel_press);
		drawright.setBounds(0, 0, drawright.getMinimumWidth()-50, drawright.getMinimumHeight()-50);
		setCompoundDrawables(draw[0], draw[1], drawright, draw[3]);
		this.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				focused = hasFocus;
				if (focused) {
					boolean isvisiable = getText().length() > 0;
					setdrawableVisiable(isvisiable);
				} else {
					setdrawableVisiable(false);
				}
			}
		});
		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				setdrawableVisiable(getText().length() > 0);

			}
		});
		setdrawableVisiable(false);
	}

	private void setdrawableVisiable(boolean visiable) {
		Drawable temp;
		if (visiable) {
			temp = drawright;
		} else {
			temp = null;
		}
		this.setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], temp, getCompoundDrawables()[3]);
	}

	// 显示一个动画,以提示用户输入
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	public Animation shakeAnimation(int CycleTimes) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
		translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
		translateAnimation.setDuration(1500);
		return translateAnimation;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:

			boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight())-5)
					&& (event.getX() < (getWidth() - getPaddingRight())-5);
			if (isClean) {
				setText("");				
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
