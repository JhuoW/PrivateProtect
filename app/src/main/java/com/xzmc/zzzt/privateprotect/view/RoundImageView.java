package com.xzmc.zzzt.privateprotect.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * By Dean.Ding
 * 
 */
public class RoundImageView extends ImageView {

	private float xRadius = 10;
	private float yRadius = 10;
	private Paint paint = new Paint();

	public RoundImageView(Context context) {
		super(context);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public float getxRadius() {
		return xRadius;
	}

	public void setxRadius(float xRadius) {
		this.xRadius = xRadius;
	}

	public float getyRadius() {
		return yRadius;
	}

	public void setyRadius(float yRadius) {
		this.yRadius = yRadius;
	}

	@SuppressLint("DrawAllocation")
	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {
		BitmapShader shader;
		if (getDrawable() instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
			shader = new BitmapShader(bitmapDrawable.getBitmap(),
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			RectF rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
			int width = bitmapDrawable.getBitmap().getWidth();
			int height = bitmapDrawable.getBitmap().getHeight();
			RectF src = null;
			if (((float) width) / height > 1) {
				src = new RectF(0.0f, 0.0f, height, height);
			} else {
				src = new RectF(0.0f, 0.0f, width, width);
			}
			Matrix matrix = canvas.getMatrix();
			matrix.setRectToRect(src, rect, Matrix.ScaleToFit.CENTER);
			shader.setLocalMatrix(matrix);

			paint.setAntiAlias(true);
			paint.setShader(shader);
			canvas.drawRoundRect(rect, this.getWidth() / 2,
					this.getHeight() / 2, paint);
		}
	}
}
