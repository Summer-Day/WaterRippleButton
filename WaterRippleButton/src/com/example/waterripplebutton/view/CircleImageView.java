package com.example.waterripplebutton.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.waterripplebutton.R;

public class CircleImageView extends ImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;
    
    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;  //园的半径

    private boolean mReady;
    private boolean mSetupPending;
    
    
    private static final int INVALIDATE_DURATION = 20; //每次刷新的时间间隔
	private static int DIFFUSE_GAP = 30;				  //扩散半径增量
	private static int TAP_TIMEOUT;					  //判断点击和长按的时间
	private Paint bottomPaint;						  //圆形底部背景色
	private Paint colorPaint;						  //圆形画笔颜色
	private Paint topTextPaint;						//上部文字
	private Paint bottomTextPaint;					//下部文字
	private int viewWidth;							  //控件宽度和高度
	private int viewHeight;                            
	private boolean isPushButton;  					  //记录是否按钮被按下
	private int maxRadio;							  //扩散的最大半径
	private int shaderRadio;						 //画圆半径的长度
	
	private int pointX;								  //控件原点坐标（左上角）
	private int pointY;
	int src_resource ;
//	static int color;	
	/*
     * 自定义文本
     */
    private float  circle_image_text_bottom_size = 1;
    private int    circle_image_text_bottom_color = Color.BLACK;
    private String circle_image_text_bottom = "";
    
    private float  circle_image_text_top_size = 1;
    private int    circle_image_text_top_color = Color.BLACK;
    private String circle_image_text_top = "";
    
    private TextView textview_bottom;
    private TextView textView_top;
    
    private RelativeLayout.LayoutParams circle_image_text_bottom_params;
    private RelativeLayout.LayoutParams circle_image_text_top_params;
    
    private static int circle_backgroud_color_start;
    private static int circle_backgroud_color_end;

    public CircleImageView(Context context) {
    	
        super(context);
        
        
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setScaleType(SCALE_TYPE);
		TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
//        src_resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
//        color = getResources().getColor(src_resource); 
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
        circle_backgroud_color_start = a.getColor(R.styleable.CircleImageView_circle_backgroud_color_start, DEFAULT_BORDER_COLOR);
        circle_backgroud_color_end = a.getColor(R.styleable.CircleImageView_circle_backgroud_color_end, DEFAULT_BORDER_COLOR);
        ALPHA = a.getInteger(R.styleable.CircleImageView_circle_backgroud_color_alpha, 50);
        set_COLOR(circle_backgroud_color_start,circle_backgroud_color_end,ALPHA);
        /**
         * 自定义文本
         */
        circle_image_text_bottom_size = a.getDimension(R.styleable.CircleImageView_circle_image_text_bottom_size, 0);
        circle_image_text_bottom_color = a.getColor(R.styleable.CircleImageView_circle_image_text_bottom_color, DEFAULT_BORDER_COLOR);
        circle_image_text_bottom = a.getString(R.styleable.CircleImageView_circle_image_text_bottom)==null?"":a.getString(R.styleable.CircleImageView_circle_image_text_bottom);
        
        circle_image_text_top_size = a.getDimension(R.styleable.CircleImageView_circle_image_text_top_size, 0);
        circle_image_text_top_color =  a.getColor(R.styleable.CircleImageView_circle_image_text_top_color, DEFAULT_BORDER_COLOR);
        circle_image_text_top= a.getString(R.styleable.CircleImageView_circle_image_text_top)==null?"":a.getString(R.styleable.CircleImageView_circle_image_text_top);
        a.recycle();
        initPaint();
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
        
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        /*
         *绘制文字 
         */
//        canvas.drawText(circle_image_text_top, 0, 0, topTextPaint);
//        canvas.drawText(circle_image_text_bottom, 100, -110, bottomPaint);
        
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        DrawTopText(canvas);
        DrawBottomText(canvas);
       
    }
    
    public void DrawTopText(Canvas canvas)
    {
    	 Rect rect = new Rect();
         topTextPaint.getTextBounds(circle_image_text_top, 0, circle_image_text_top.length(), rect);
         canvas.drawText(circle_image_text_top, ((mBorderRadius*2-rect.right+rect.left)/2)-rect.left,(float) (mBorderRadius-rect.bottom), topTextPaint);
    }
    
    public void DrawBottomText(Canvas canvas)
    {
    	 Rect rect = new Rect();
         bottomTextPaint.getTextBounds(circle_image_text_bottom, 0, circle_image_text_bottom.length(), rect);
         canvas.drawText(circle_image_text_bottom, ((mBorderRadius*2-rect.right+rect.left)/2)-rect.left,(float) ((1.15*mBorderRadius)-rect.top), bottomTextPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
		this.viewWidth = w;
		this.viewHeight = h;
        setup();
    }

    /**
     * get circle paint color
     * @return
     */
    public int getBorderColor() {
        return mBorderColor;
    }
    /**
     * set circle paint color 
     * @param borderColor
     */
    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);

        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 水波纹
     */
	/**
	 * 初始化画笔资源
	 */
    int ALPHA =50;
	private void initPaint() {
		colorPaint = new Paint();
		bottomPaint = new Paint();
		colorPaint.setColor(TEXT_START_COLOR);
		colorPaint.setAlpha(ALPHA);//设置alpha不透明度，范围为0~255
		bottomPaint.setColor(Color.YELLOW);
		bottomPaint.setAlpha(30);
		
		/*
		 *定义上下文字画笔 
		 */
		topTextPaint = new Paint();
		topTextPaint.setTextSize(circle_image_text_top_size);
		topTextPaint.setColor(circle_image_text_top_color);
		bottomTextPaint = new Paint();
		bottomTextPaint.setTextSize(circle_image_text_bottom_size);
		bottomTextPaint.setColor(circle_image_text_bottom_color);
		
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
//		if(viewWidth > viewHeight){
//			if(eventX < viewWidth / 2){
//				maxRadio = viewWidth - eventX;
//			}else{
//				maxRadio = viewWidth;
//			}
//		}else{
//			if(eventY < viewHeight / 2){
//				maxRadio = viewHeight;
//			}else{
//				maxRadio = viewWidth;//正方形
				maxRadio =(int) ((viewHeight / 2) +
						Math.sqrt(Math.pow(eventX, 2)+Math.pow(eventY, 2)));//圆形
//			}
//		}
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
		
		//绘制按下后的整个背景
//		canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight, bottomPaint);
//		canvas.save();
		
		
//		canvas.drawCircle(viewWidth/2, viewHeight/2, viewWidth/2, bottomPaint);
		Path mPath=new Path();
		mPath.reset();
		canvas.clipPath(mPath); // makes the clip empty
		mPath.addCircle(viewWidth/2, viewHeight/2, mBorderRadius, Path.Direction.CCW);
		canvas.clipPath(mPath, Region.Op.REPLACE);
		if(colorPaint == null)
		{
			colorPaint = new Paint();
		}
		colorPaint.setColor(getNewColor((float)shaderRadio/maxRadio));
		colorPaint.setAlpha(ALPHA);
		canvas.save();
		//绘制扩散圆形背景
//		canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY + viewHeight);
		canvas.drawCircle(eventX, eventY, shaderRadio, colorPaint);
		canvas.restore();
//        Rect rect = new Rect();
//        topTextPaint.getTextBounds(circle_image_text_top, 0, circle_image_text_top.length(), rect);
//        canvas.drawText(circle_image_text_top, -rect.left,-rect.top, topTextPaint);
		DrawTopText(canvas);
		DrawBottomText(canvas);
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
	private   int TEXT_START_COLOR=Color.parseColor("#3333FF");

	private  int END_COLOR =Color.parseColor("#CC00FF") ;
//	private final static int END_COLOR=Color.parseColor("#99CC66");
//	private  int END_COLOR=-16711936;

	private  int TEXT_START_R=Color.red(TEXT_START_COLOR);

	private  int TEXT_START_G=Color.green(TEXT_START_COLOR);

	private  int TEXT_START_B=Color.blue(TEXT_START_COLOR);

	private  int TEXT_DIF_R=Color.red(END_COLOR)-TEXT_START_R;

	private  int TEXT_DIF_G=Color.green(END_COLOR)-TEXT_START_G;

	private  int TEXT_DIF_B=Color.blue(END_COLOR)-TEXT_START_B;
	public void set_COLOR(int TEXT_START_COLOR,int END_COLOR,int ALPHA)
	{
		this.END_COLOR = END_COLOR;
		this.TEXT_START_COLOR = TEXT_START_COLOR;
		this.ALPHA = ALPHA;
		TEXT_START_R=Color.red(TEXT_START_COLOR);

		TEXT_START_G=Color.green(TEXT_START_COLOR);

		TEXT_START_B=Color.blue(TEXT_START_COLOR);

		TEXT_DIF_R=Color.red(END_COLOR)-TEXT_START_R;

		TEXT_DIF_G=Color.green(END_COLOR)-TEXT_START_G;

		TEXT_DIF_B=Color.blue(END_COLOR)-TEXT_START_B;
	}
	/**            
	 * @param f 		颜色渐变参考值。
	 */
	private int getNewColor(float f){
		int newR,newG,newB;
		newR=(int)(TEXT_DIF_R*f)+TEXT_START_R;
		newG=(int)(TEXT_DIF_G*f)+TEXT_START_G;
		newB=(int)(TEXT_DIF_B*f)+TEXT_START_B;
		return Color.rgb(newR, newG, newB);
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
	 final float scale = context.getResources().getDisplayMetrics().density;
	 return (int) (dpValue * scale + 0.5f);
	}

	 

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
	 final float scale = context.getResources().getDisplayMetrics().density;
	 return (int) (pxValue / scale + 0.5f);
	}
}