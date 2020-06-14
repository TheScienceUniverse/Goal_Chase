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

import static java.lang.Integer.parseInt;

public class DeviceIdView extends View {
	private Paint paint = null;
	private Utility utility = new Utility (getContext());
	private Pixel [] pixels = new Pixel [64];
	private int width = 256;
	private int height = 256;

	public DeviceIdView (Context context) {
		super (context);

		this.setLayoutParams (new WindowManager.LayoutParams(width, height));
		this.init (null);
	}

	public DeviceIdView (Context context, int percent) {
		super (context);
		init (null);
	}

	public DeviceIdView (Context context, @Nullable AttributeSet attrs) {
		super (context, attrs);
		init (attrs);
	}

	public DeviceIdView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super (context, attrs, defStyleAttr);
		init (attrs);
	}

	public DeviceIdView (Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super (context, attrs, defStyleAttr, defStyleRes);
		init (attrs);
	}

	private void init (@Nullable AttributeSet attributeSet) {
		this.paint = new Paint();
		this.paint.setAntiAlias (true);
		this.paint.setDither (true);
		this.paint.setStyle (Paint.Style.STROKE);
		this.paint.setStrokeJoin (Paint.Join.ROUND);
		this.paint.setStrokeCap (Paint.Cap.ROUND);
		this.paint.setStrokeWidth (64);
		this.createImageData();

		Context context = getContext();
		WindowManager windowManager = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics (displayMetrics);

		postInvalidate();
	}

	@Override
	protected void onDraw (Canvas canvas) {
		super.onDraw(canvas);

		int boxSide = 32;
		int x, y, index;

		for (x = 0; x < 8; x++) {
			for (y = 0; y < 8; y++) {
				index = 8 * x + y;
				this.paint.setStyle (Paint.Style.FILL);
				this.paint.setColor (Color.argb (this.pixels [index].alpha, this.pixels [index].red, this.pixels [index].green, this.pixels [index].blue));
				canvas.drawRect (boxSide * x, boxSide * y, boxSide * (x + 1), boxSide * (y + 1), this.paint);
			}
		}
	}

	private void createImageData() {
		String hash = this.utility.getDeviceId();

		int x, y, index;
		int a, r, g, b;


		for (x = 0; x < 8; x++) {
			for (y = 0; y < 8; y++) {
				index = 8 * x + y;

				if (x == 0) {
					a = parseInt (hash.substring (index, index + 2), 16);
					r = parseInt (hash.substring (index + 2, index + 4), 16);
					g = parseInt (hash.substring (index + 4, index + 6), 16);
					b = parseInt (hash.substring (index + 6, index + 8), 16);
				} else {
					a = this.pixels [(x + y) % 8].alpha;
					r = this.pixels [(x + y) % 8].red;
					g = this.pixels [(x + y) % 8].green;
					b = this.pixels [(x + y) % 8].blue;
				}

				this.pixels [index] = new Pixel (a, r, g, b);
			}
		}
	}

	static class Pixel {
		public int alpha;
		public int red;
		public int green;
		public int blue;

		public Pixel (int a, int r, int g, int b) {
			this.alpha = a;
			this.red = r;
			this.green = g;
			this.blue = b;
		}
	}
}