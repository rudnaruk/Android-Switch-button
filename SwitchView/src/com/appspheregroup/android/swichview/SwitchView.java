/*
Copyright 2013 Appsphere Group

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
	 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */

package com.appspheregroup.android.swichview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/**
 * @since 01/2013
 * @version 1.0
 * @author Winnaruk
 *
 */
public class SwitchView extends View {
	private TextPaint mTextPaint;
	private String mText_on = "ON";
	private String mText_off = "OFF";
	private Rect text_bounds = new Rect();
	private float density = 1f;
	private Switch mSwitch;
	private boolean isOn = true;
	private Bitmap img;
	final static int DEFAULT_TEXT_SIZE = 15;
	private boolean isTouch = false;
	private int PADDING = 0;
	private int lastX,lastY;
	private OnSwitchChangeListener mSwitchChangeListener;
	private NinePatchDrawable npd;
	private NinePatchDrawable npd1;
	private boolean isStateListDrawable =false;
	private StateListDrawable stateListDrawable;
	private Rect npdBounds;
	private Rect npdBounds1;
	public SwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}
	
	public SwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public void setTextOn(String mText_on) {
		this.mText_on = mText_on;
		updateTextBound();
	}

	public void setTextOff(String mText_off) {
		this.mText_off = mText_off;
		updateTextBound();
	}
	private void updateTextBound(){
		String mString=null;
		if(mText_on.length()>=mText_off.length()){
			mString= mText_on;
		}else{
			mString =mText_off;
		}
		mTextPaint.getTextBounds(mString, 0, mString.length(), text_bounds);
	}
	private void init(Context context, AttributeSet attrs){
		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(DEFAULT_TEXT_SIZE);
		mTextPaint.setColor(Color.WHITE);
		img = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.com_appsphere_button_switch);
		this.npd = (NinePatchDrawable) getResources().getDrawable(R.drawable.com_appsphere_bg_switch_act);
		this.npd1 = (NinePatchDrawable) getResources().getDrawable(R.drawable.com_appsphere_bg_switch_inact);
		TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.SwitchView);
		int indexCount=a.getIndexCount();
		for(int i=0;i<indexCount;++i)
		  {
			  final int attr=a.getIndex(i);
			  switch (attr) {
				case R.styleable.SwitchView_text_on:
					mText_on=a.getString(attr);
					break;
				case R.styleable.SwitchView_text_off:
					mText_off=a.getString(attr);
					break;
				case R.styleable.SwitchView_background_switch:
					if(a.getDrawable(attr) instanceof StateListDrawable){
						isStateListDrawable =true;
						stateListDrawable =(StateListDrawable) a.getDrawable(attr);
						stateListDrawable.mutate(); // make sure that we aren't sharing state anymore

			            ConstantState constantState = stateListDrawable.getConstantState();
			            if (constantState instanceof DrawableContainerState) {
			                DrawableContainerState drwblContainerState = (DrawableContainerState)constantState;
			                final Drawable[] drawables = drwblContainerState.getChildren();
			                Drawable drwbl =drawables[0];
			                if (drwbl instanceof BitmapDrawable)
			                    	img = ((BitmapDrawable)drwbl).getBitmap();
			            }
					}else{
						Drawable d = a.getDrawable(attr);
						BitmapDrawable drawable =(BitmapDrawable)d;
		            	img = drawable.getBitmap();
					}
					break;
				case R.styleable.SwitchView_background_act:
					this.npd = (NinePatchDrawable)a.getDrawable(attr);
					break;
				case R.styleable.SwitchView_background_inact:
					this.npd1 = (NinePatchDrawable)a.getDrawable(attr);
					break;
				default:
					break;
			  }
		}
		a.recycle();
		mSwitch = new Switch(0, 0, img);
		setPadding(PADDING, 0, PADDING, 0);
		npdBounds = new Rect();
		npdBounds1 =new Rect();
		mTextPaint.setTextAlign(Align.CENTER);
	}

	public void setOnSwitchChangeListener(
			OnSwitchChangeListener mSwitchChangeListener) {
		this.mSwitchChangeListener = mSwitchChangeListener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		if (isOn && !isTouch) {
			mSwitch.setX(getWidth() - img.getWidth() - getPaddingRight());
		} else if (!isOn && !isTouch) {
			mSwitch.setX(getPaddingLeft());
		}
		// Set its bound where you need
		npdBounds.set(getPaddingLeft()+3, getPaddingTop()+4, getWidth()- getPaddingRight() - img.getWidth() / 2, getHeight()- getPaddingBottom()-3);
		npd.setBounds(npdBounds);
		// Finally draw on the canvas
		npd.draw(canvas);

		
		// Set its bound where you need
		npdBounds1.set(mSwitch.getX() + img.getWidth() / 2,getPaddingTop()+4, getWidth() - getPaddingRight() -3, getHeight()- getPaddingBottom()-3);
		npd1.setBounds(npdBounds1);
		// Finally draw on the canvas
		npd1.draw(canvas);

		float text_horizontally_centered_origin_x = getWidth() / 2;
		float text_horizontally_centered_origin_y = getHeight() / 2;
		canvas.translate(text_horizontally_centered_origin_x,
				text_horizontally_centered_origin_y);
		canvas.drawText(
				(mSwitch.getX() >= (getWidth() - getPaddingLeft() - getPaddingRight()) / 2) ? mText_on
						: mText_off,
				getPaddingLeft(), text_bounds.height() / 2, mTextPaint);
		canvas.restore();
		mSwitch.onDraw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mTextPaint.getTextBounds(mText_on, 0, mText_on.length(), text_bounds);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = Math.max(text_bounds.width() + getPaddingLeft() + getPaddingRight()
					+ (int)(mSwitch.width * 2),(int) mSwitch.width *2);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = Math.max(text_bounds.height() + getPaddingTop()
					+ getPaddingBottom(), (int) mSwitch.height);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}
	public void setSwitchOn(boolean isOn){
		this.isOn=isOn;
		if(isStateListDrawable){
			
			int[] state =null ;
			if(isOn){
				state= new int[] {android.R.attr.state_selected};
			}else{
				state= new int[] {android.R.attr.state_active};
			}
			stateListDrawable.setState(state);
            ConstantState constantState = stateListDrawable.getConstantState();
            if (constantState instanceof DrawableContainerState) {
                DrawableContainerState drwblContainerState = (DrawableContainerState)constantState;
                final Drawable[] drawables = drwblContainerState.getChildren();
                Drawable drwbl =(isOn)?drawables[0]:drawables[1];
                if (drwbl instanceof BitmapDrawable)
                    	img = ((BitmapDrawable)drwbl).getBitmap();
            }
            mSwitch.setImg(img);
		}
		invalidate();
	}
	public boolean isSwitchOn(){
		return isOn;
	}
	public void setOnOffString(String[] stringOnOff){
		if(stringOnOff.length>=1)
			mText_on=stringOnOff[0];
		if(stringOnOff.length>=2)
			mText_off=stringOnOff[1];
	}
	public void setBackgroundSwitchOn(int id){
		try{
			npd = (NinePatchDrawable) getResources().getDrawable(id);
		}catch (ClassCastException e) {
			Log.e("SwitchView","Can not set image to background switch on");
		}
	}
	public void setBackgroundSwitchOff(int id){
		try{
			npd1 = (NinePatchDrawable) getResources().getDrawable(id);
		}catch (ClassCastException e) {
			Log.e("SwitchView","Can not set image to background switch off");
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction 	= event.getAction();
		int X 	= (int) event.getX();
		int Y 	= (int) event.getY();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN:
			if(mSwitch.isCollision(X, Y)){
				isTouch=true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
				if(isTouch){
					if (X > (getPaddingLeft()) && X < getWidth()-getPaddingRight()-img.getWidth()) {
						mSwitch.setX(X);
					}
					lastX=X;
					lastY=Y;
				}
			break;
		case MotionEvent.ACTION_UP:
			if(isTouch){
				if(X>=(getWidth()-getPaddingLeft()-getPaddingRight())/2){
					isOn=true;
				}else{
					isOn=false;
				}
				if(mSwitchChangeListener!=null){
					mSwitchChangeListener.onSwitchChanged(SwitchView.this, isOn);
				}
				if(isStateListDrawable){
		            ConstantState constantState = stateListDrawable.getConstantState();
		            if (constantState instanceof DrawableContainerState) {
		                DrawableContainerState drwblContainerState = (DrawableContainerState)constantState;
		                final Drawable[] drawables = drwblContainerState.getChildren();
		                Drawable drwbl =(isOn)?drawables[0]:drawables[1];
		                if (drwbl instanceof BitmapDrawable)
		                    	img = ((BitmapDrawable)drwbl).getBitmap();
		            }
		            mSwitch.setImg(img);
				}
				isTouch=false;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			if(isTouch){
				if(lastX>=(getWidth()-getPaddingLeft()-getPaddingRight())/2){
					isOn=true;
				}else{
					isOn=false;
				}
				if(mSwitchChangeListener!=null){
					mSwitchChangeListener.onSwitchChanged(SwitchView.this, isOn);
				}
				if(isStateListDrawable){
					
					int[] state =null ;
					if(isOn){
						state= new int[] {android.R.attr.state_selected};
					}else{
						state= new int[] {android.R.attr.state_active};
					}
					stateListDrawable.setState(state);
		            ConstantState constantState = stateListDrawable.getConstantState();
		            if (constantState instanceof DrawableContainerState) {
		                DrawableContainerState drwblContainerState = (DrawableContainerState)constantState;
		                final Drawable[] drawables = drwblContainerState.getChildren();
		                Drawable drwbl =(isOn)?drawables[0]:drawables[1];
		                if (drwbl instanceof BitmapDrawable)
		                    	img = ((BitmapDrawable)drwbl).getBitmap();
		            }
		            mSwitch.setImg(img);
				}
				isTouch=false;
			}
			break;
		}
		invalidate();
		return true;
	}



	class Switch {
		private int x,y,width,height;
		private Bitmap img;
		public Switch(int x, int y, Bitmap img) {
			this.x	= x;
			this.y	= y;
			this.img= img;
			width	= img.getWidth();
			height	= img.getHeight();
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public Bitmap getImg() {
			return img;
		}
		public void setImg(Bitmap img) {
			this.img = img;
		}
		public void onDraw(Canvas canvas){
			canvas.drawBitmap(img, x, y, null);
		}
		public boolean isCollision(float x2,float y2){
			Log.d("Rut touch", ""+x2+","+y2);
			Log.d("event touch", ""+x+""+width +","+y+height);
			return x2 > x && x2 < (x +width) && y2 > (y) && y2 < (y +height) ;
		}
	}
	public interface OnSwitchChangeListener {
		public void onSwitchChanged(View view, boolean isOn);
	}

}
