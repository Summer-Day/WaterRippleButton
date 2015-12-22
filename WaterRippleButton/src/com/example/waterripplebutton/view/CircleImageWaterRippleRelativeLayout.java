//package com.example.waterripplebutton.view;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.util.AttributeSet;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.waterripplebutton.R;
//
//public class CircleImageWaterRippleRelativeLayout extends RelativeLayout{
//	/*
//     * 自定义文本
//     */
//    private float  circle_image_text_bottom_size;
//    private int    circle_image_text_bottom_color;
//    private String circle_image_text_bottom;
//    
//    private float  circle_image_text_top_size;
//    private int    circle_image_text_top_color;
//    private String circle_image_text_top;
//    
//    private TextView textview_bottom;
//    private TextView textView_top;
//    
//    private RelativeLayout.LayoutParams circle_image_text_bottom_params;
//    private RelativeLayout.LayoutParams circle_image_text_top_params;
//    private RelativeLayout.LayoutParams circleImageWaterRippleRelativeLayout_param;  
//    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;
//	
//    public CircleImageWaterRippleRelativeLayout(Context context) {
//        super(context);
//    }
//
//    public CircleImageWaterRippleRelativeLayout(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//	public CircleImageWaterRippleRelativeLayout(Context context,
//			AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//		 TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageWaterRippleRelativeLayout, defStyle, 0);
//		 circle_image_text_bottom_size = a.getDimension(R.styleable.CircleImageWaterRippleRelativeLayout_circle_image_text_bottom_size, 0);
//	        circle_image_text_bottom_color = a.getColor(R.styleable.CircleImageWaterRippleRelativeLayout_circle_image_text_bottom_color, DEFAULT_BORDER_COLOR);
//	        circle_image_text_bottom = a.getString(R.styleable.CircleImageWaterRippleRelativeLayout_circle_image_text_bottom);
//	        
//	        circle_image_text_top_size = a.getDimension(R.styleable.CircleImageWaterRippleRelativeLayout_circle_image_text_top_size, 0);
//	        circle_image_text_top_color =  a.getColor(R.styleable.CircleImageWaterRippleRelativeLayout_circle_image_text_top_color, DEFAULT_BORDER_COLOR);
//	        circle_image_text_top= a.getString(R.styleable.CircleImageWaterRippleRelativeLayout_circle_image_text_top);
//
//	        a.recycle();
//	        
//	        textview_bottom = new TextView(context);
//	        textView_top =  new TextView(context);
//	        
//	        textview_bottom.setText(circle_image_text_bottom);
//	        textview_bottom.setTextColor(circle_image_text_bottom_color);
//	        textview_bottom.setTextSize(circle_image_text_bottom_size);
//	        
//	        textView_top.setText(circle_image_text_top);
//	        textView_top.setTextColor(circle_image_text_top_color);
//	        textView_top.setTextSize(circle_image_text_top_size);
//	        
//	        
////	        circle_image_text_top_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//	        
//	        circleImageWaterRippleRelativeLayout_param =new RelativeLayout.LayoutParams(500,500);
//	        circleImageWaterRippleRelativeLayout_param.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		
//	        CircleImageView cwCircleImage = new CircleImageView(context);
//	        cwCircleImage.setImageDrawable(getResources().getDrawable(R.drawable.yx));
//	        cwCircleImage.setImageResource(R.color.green);
//	        Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.yx);
//	        cwCircleImage.setImageBitmap(bmp);
//	        R.color.green
//	        getResources().getDrawable(R.drawable.yx)
//	        CircleImageView.setBackgroundDrawable();
//	        addView(cwCircleImage,circleImageWaterRippleRelativeLayout_param);
//	
//	        circle_image_text_bottom_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//	        circle_image_text_bottom_params.addRule(RelativeLayout.CENTER_VERTICAL);
//	        circle_image_text_bottom_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//	        addView(textview_bottom,circle_image_text_bottom_params);
//	}
//
//
//
//}
