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

	private static final int INVALIDATE_DURATION = 50; //ÿ��ˢ�µ�ʱ����
	private static int DIFFUSE_GAP = 10;				  //��ɢ�뾶����
	private static int TAP_TIMEOUT;					  //�жϵ���ͳ�����ʱ��
	private Paint bottomPaint;						  //����
	private Paint colorPaint;
	private int viewWidth;							  //�ؼ���Ⱥ͸߶�
	private int viewHeight;                            
	private boolean isPushButton;  					  //��¼�Ƿ�ť������
	private int maxRadio;							  //��ɢ�����뾶
	private int shaderRadio;						 //��Բ�뾶�ĳ���
	
	private int pointX;								  //�ؼ�ԭ�����꣨���Ͻǣ�
	private int pointY;
	
	
	

	/**
	 * ��ʼ��������Դ
	 */
	private void initPaint() {
		colorPaint = new Paint();
		bottomPaint = new Paint();
		colorPaint.setColor(Color.BLUE);
		colorPaint.setAlpha(20);//����alpha��͸���ȣ���ΧΪ0~255
		bottomPaint.setColor(Color.YELLOW);
		bottomPaint.setAlpha(30);
	}
	
	/*
	 * ע�⣺ ��ondraw()ǰ����
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
			//ֻ��Ҫȡһ��ʱ��
			if(downTime == 0){
				downTime = SystemClock.elapsedRealtime();
			}
			eventX = (int)event.getX();
			eventY = (int)event.getY();
			//�������뾶
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
	 * �����ʱ�����뾶
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
				maxRadio =(int)Math.sqrt(Math.pow(eventX, 2)+Math.pow(eventY, 2));//Բ��
			}
		}
	}
	/**
	 * ����ı�����ݣ���ʼ�����ݣ�
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
		if(!isPushButton) return; //�����ťû�б������򷵻�
		
//		//���ư��º����������
//		canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight, bottomPaint);
//		canvas.save();
		
//		Path mPath=new Path();
//		mPath.reset();
//		canvas.clipPath(mPath); // makes the clip empty
//		mPath.addCircle(viewWidth/2, viewWidth/2, viewWidth/2, Path.Direction.CCW);
//		canvas.clipPath(mPath, Region.Op.REPLACE);
//		canvas.save();
		
		//������ɢԲ�α���
		canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight);
		canvas.drawCircle(eventX, eventY, shaderRadio, colorPaint);
		canvas.restore();
		//ֱ���뾶�������뾶
		if(shaderRadio < maxRadio){
			postInvalidateDelayed(INVALIDATE_DURATION, 
					pointX, pointY, pointX + viewWidth, pointY + viewHeight);
			shaderRadio += DIFFUSE_GAP;
		}else{
			clearData();
		}
	}

}
