package com.example.waterripplebutton.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Button;



public class WaterRippleButtonView extends Button{
	
	public WaterRippleButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
		TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
		// TODO Auto-generated constructor stub
	}

	private static final int INVALIDATE_DURATION = 50; //每次刷新的时间间隔
	private static int DIFFUSE_GAP = 10;				  //扩散半径增量
	private static int TAP_TIMEOUT;					  //判断点击和长按的时间
	private Paint bottomPaint;						  //画笔
	private Paint colorPaint;
	private int viewWidth;							  //控件宽度和高度
	private int viewHeight;                            
	private boolean isPushButton;  					  //记录是否按钮被按下
	private int maxRadio;							  //扩散的最大半径
	private int shaderRadio;						 //画圆半径的长度
	
	private int pointX;								  //控件原点坐标（左上角）
	private int pointY;
	
	
	

	/**
	 * 初始化画笔资源
	 */
	private void initPaint() {
		colorPaint = new Paint();
		bottomPaint = new Paint();
		colorPaint.setColor(Color.BLUE);
		colorPaint.setAlpha(20);//设置alpha不透明度，范围为0~255
		bottomPaint.setColor(Color.YELLOW);
		bottomPaint.setAlpha(30);
	}
	
	/*
	 * 注意： 在ondraw()前调用
	 * (non-Javadoc)
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		this.viewWidth = w;
		this.viewHeight = h;
	}
	
	private int eventX;
	private int eventY;
	private long downTime = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//只需要取一次时间
			if(downTime == 0){
				downTime = SystemClock.elapsedRealtime();
			}
			eventX = (int)event.getX();
			eventY = (int)event.getY();
			//计算最大半径
			countMaxRadio();
			isPushButton = true;
			postInvalidateDelayed(INVALIDATE_DURATION);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if(SystemClock.elapsedRealtime() - downTime < TAP_TIMEOUT){
				DIFFUSE_GAP = 30;
				postInvalidate();
			}else{
				clearData();
			}
			break;
		}
		return true;
	}

	
	/**
	 * 计算此时的最大半径
	 */
	private void countMaxRadio() {
		if(viewWidth > viewHeight){
			if(eventX < viewWidth / 2){
				maxRadio = viewWidth - eventX;
			}else{
				maxRadio = 2*eventX;
			}
		}else{
			if(eventY < viewHeight / 2){
				maxRadio = viewHeight - eventY;
			}else{
				maxRadio =(int)Math.sqrt(Math.pow(eventX, 2)+Math.pow(eventY, 2));//圆形
			}
		}
	}
	/**
	 * 清理改变的数据（初始化数据）
	 */
	private void clearData(){
		downTime = 0;
		DIFFUSE_GAP = 10;
		isPushButton = false;
		shaderRadio = 0;
		postInvalidate();
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if(!isPushButton) return; //如果按钮没有被按下则返回
		
//		//绘制按下后的整个背景
//		canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight, bottomPaint);
//		canvas.save();
		
//		Path mPath=new Path();
//		mPath.reset();
//		canvas.clipPath(mPath); // makes the clip empty
//		mPath.addCircle(viewWidth/2, viewWidth/2, viewWidth/2, Path.Direction.CCW);
//		canvas.clipPath(mPath, Region.Op.REPLACE);
//		canvas.save();
		
		//绘制扩散圆形背景
		canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight);
		canvas.drawCircle(eventX, eventY, shaderRadio, colorPaint);
		canvas.restore();
		//直到半径等于最大半径
		if(shaderRadio < maxRadio){
			postInvalidateDelayed(INVALIDATE_DURATION, 
					pointX, pointY, pointX + viewWidth, pointY + viewHeight);
			shaderRadio += DIFFUSE_GAP;
		}else{
			clearData();
		}
	}

}
