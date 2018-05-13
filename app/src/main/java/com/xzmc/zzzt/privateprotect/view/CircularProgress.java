package com.xzmc.zzzt.privateprotect.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.xzmc.health.R;


@SuppressLint("NewApi")
public class CircularProgress extends View {

	private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
	private static final Interpolator SWEEP_INTERPOLATOR = new AccelerateDecelerateInterpolator();
	private static final int ANGLE_ANIMATOR_DURATION = 2000;
	private static final int SWEEP_ANIMATOR_DURATION = 900;
	private static final int MIN_SWEEP_ANGLE = 30;
	private static final int DEFAULT_BORDER_WIDTH = 3;
	private final RectF fBounds = new RectF();

	private ObjectAnimator mObjectAnimatorSweep;
	private ObjectAnimator mObjectAnimatorAngle;
	private boolean mModeAppearing = true;
	private Paint mPaint;
	private float mCurrentGlobalAngleOffset;
	private float mCurrentGlobalAngle;
	private float mCurrentSweepAngle;
	private float mBorderWidth;
	private boolean mRunning;
	private int[] mColors = { 0xFF32b16c, 0xFF32b16c, 0xFF32b16c, 0xFF32b16c,
			0xFF32b16c, 0xFF32b16c, 0xFF32b16c };

	//private int[] mColors = { R.color.theme };

	private int mCurrentColorIndex;
	private int mNextColorIndex;

	public CircularProgress(Context context) {
		this(context, null);
	}

	public CircularProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircularProgress(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		float density = context.getResources().getDisplayMetrics().density;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircularProgress, defStyleAttr, 0);
		mBorderWidth = a.getDimension(R.styleable.CircularProgress_borderWidth,
				DEFAULT_BORDER_WIDTH * density);
		a.recycle();

		mCurrentColorIndex = 0;
		mNextColorIndex = 1;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setColor(mColors[mCurrentColorIndex]);

		setupAnimations();
	}

	private void start() {
		if (isRunning()) {
			return;
		}
		mRunning = true;
		mObjectAnimatorAngle.start();
		mObjectAnimatorSweep.start();
		invalidate();
	}

	private void stop() {
		if (!isRunning()) {
			return;
		}
		mRunning = false;
		mObjectAnimatorAngle.cancel();
		mObjectAnimatorSweep.cancel();
		invalidate();
	}

	private boolean isRunning() {
		return mRunning;
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (visibility == VISIBLE) {
			start();
		} else {
			stop();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		start();
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		stop();
		super.onDetachedFromWindow();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		fBounds.left = mBorderWidth / 2f + .5f;
		fBounds.right = w - mBorderWidth / 2f - .5f;
		fBounds.top = mBorderWidth / 2f + .5f;
		fBounds.bottom = h - mBorderWidth / 2f - .5f;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
		float sweepAngle = mCurrentSweepAngle;
		if (mModeAppearing) {
			mPaint.setColor(gradient(mColors[mCurrentColorIndex],
					mColors[mNextColorIndex], mCurrentSweepAngle
							/ (360 - MIN_SWEEP_ANGLE * 2)));
			sweepAngle += MIN_SWEEP_ANGLE;
		} else {
			startAngle = startAngle + sweepAngle;
			sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
		}
		canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
	}

	private static int gradient(int color1, int color2, float p) {
		int r1 = (color1 & 0xff0000) >> 16;
		int g1 = (color1 & 0xff00) >> 8;
		int b1 = color1 & 0xff;
		int r2 = (color2 & 0xff0000) >> 16;
		int g2 = (color2 & 0xff00) >> 8;
		int b2 = color2 & 0xff;
		int newr = (int) (r2 * p + r1 * (1 - p));
		int newg = (int) (g2 * p + g1 * (1 - p));
		int newb = (int) (b2 * p + b1 * (1 - p));
		return Color.argb(255, newr, newg, newb);
	}

	private void toggleAppearingMode() {
		mModeAppearing = !mModeAppearing;
		if (mModeAppearing) {
			mCurrentColorIndex = ++mCurrentColorIndex % mColors.length;
			mNextColorIndex = ++mNextColorIndex % mColors.length;
			mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	// ////////////// Animation

	private Property<CircularProgress, Float> mAngleProperty = new Property<CircularProgress, Float>(
			Float.class, "angle") {
		@Override
		public Float get(CircularProgress object) {
			return object.getCurrentGlobalAngle();
		}

		@Override
		public void set(CircularProgress object, Float value) {
			object.setCurrentGlobalAngle(value);
		}
	};

	private Property<CircularProgress, Float> mSweepProperty = new Property<CircularProgress, Float>(
			Float.class, "arc") {
		@Override
		public Float get(CircularProgress object) {
			return object.getCurrentSweepAngle();
		}

		@Override
		public void set(CircularProgress object, Float value) {
			object.setCurrentSweepAngle(value);
		}
	};

	private void setupAnimations() {
		mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty,
				360f);
		mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
		mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
		mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);
		mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);

		mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty,
				360f - MIN_SWEEP_ANGLE * 2);
		mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
		mObjectAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
		mObjectAnimatorSweep.setRepeatMode(ValueAnimator.RESTART);
		mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
		mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				toggleAppearingMode();
			}
		});
	}

	public void setCurrentGlobalAngle(float currentGlobalAngle) {
		mCurrentGlobalAngle = currentGlobalAngle;
		invalidate();
	}

	public float getCurrentGlobalAngle() {
		return mCurrentGlobalAngle;
	}

	public void setCurrentSweepAngle(float currentSweepAngle) {
		mCurrentSweepAngle = currentSweepAngle;
		invalidate();
	}

	public float getCurrentSweepAngle() {
		return mCurrentSweepAngle;
	}
}
