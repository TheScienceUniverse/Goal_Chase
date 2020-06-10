package dev.the01guy.goal_chase.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class ProgressView extends View {
	private Paint paint = null;

	public ProgressView (Context context) {
		super (context);
		init (null);
	}

	public ProgressView (Context context, @Nullable AttributeSet attrs) {
		super (context, attrs);
		init (attrs);
	}

	public ProgressView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super (context, attrs, defStyleAttr);
		init (attrs);
	}

	public ProgressView (Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super (context, attrs, defStyleAttr, defStyleRes);
		init (attrs);
	}

	private void init (@Nullable AttributeSet attributeSet) {
		this.paint = new Paint();
		this.paint.setAntiAlias (true);
		this.paint.setDither (true);
		this.paint.setColor (Color.RED);
		this.paint.setStyle (Paint.Style.STROKE);
		this.paint.setStrokeJoin (Paint.Join.ROUND);
		this.paint.setStrokeCap (Paint.Cap.ROUND);
		this.paint.setStrokeWidth (64);

		postInvalidate();
	}

	@Override
	protected void onDraw (Canvas canvas) {
		super.onDraw(canvas);

		Context context = getContext();
		WindowManager windowManager = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics (displayMetrics);

		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;

		float cx = (float) width / 2;
		float cy = (float) height / 2;

		int perent = 1;

		double degree = 1.8 * perent;
		float x, y, r = 300;

		for (int i = 0; i < degree; i++) {
			x = (float) (cx - r * Math.cos (i * Math.PI / 180));
			y = (float) (cy - r * Math.sin (i * Math.PI / 180));
			canvas.drawPoint(x, y, paint);
		}

		double percent = Math.floor (100.00 / 180);
		this.paint.setStrokeWidth (1);
		this.paint.setTextSize(100);
		this.paint.setStyle(Paint.Style.FILL);

		int textWidth = (int) paint.measureText("X");
		canvas.drawText("100%", cx - 2 * textWidth, cy - 50, paint);
	}
}