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

import dev.the01guy.goal_chase.utility.Utility;

public class ProgressView extends View {
	private Paint paint = null;
	private int percent = 0;
	private Utility utility = new Utility (getContext());

	public ProgressView (Context context) {
		super (context);
		init (null);
	}

	public ProgressView (Context context, int percent) {
		super (context);
		this.percent = percent;
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

		double degree = 1.8 * this.percent;
		float x, y, r = 300;

		for (int i = 0; i < degree; i++) {
			x = (float) (cx - r * Math.cos (i * Math.PI / 180));
			y = (float) (cy - r * Math.sin (i * Math.PI / 180));
			canvas.drawPoint(x, y, this.paint);
		}

		String string = "" + this.percent;
		string = this.utility.padLeftZeros(string, 3) + "%";

		this.paint.setStrokeWidth (32);
		this.paint.setTextSize (100);
		this.paint.setStyle (Paint.Style.FILL);

		int textWidth = (int) this.paint.measureText ("X");
		canvas.drawText (string, cx - 2 * textWidth, cy - 50, this.paint);
	}
}