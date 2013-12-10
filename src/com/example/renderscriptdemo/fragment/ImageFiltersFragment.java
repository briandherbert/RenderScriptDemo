package com.example.renderscriptdemo.fragment;

import ca.tutortutor.grayscale.ScriptC_grayscale;
import ca.tutortutor.wavyimage.ScriptC_wavy;

import com.example.renderscriptdemo.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.support.v8.renderscript.*;

public class ImageFiltersFragment extends Fragment implements OnClickListener {
	public static final String TAG = "RenderScript Demo";

	public enum ImageFilter {
		GAUSS(true), CONVOLVE(true), GRAY(true), GRAY_RS(false), WAVY(false);

		boolean isItrinsic;

		ImageFilter(boolean isIntrinsic) {
			this.isItrinsic = isIntrinsic;
		}
	}

	// Intrinsics
	ScriptIntrinsicBlur scriptBlur;
	ScriptIntrinsicConvolve3x3 scriptConvolve;
	ScriptIntrinsicColorMatrix scriptGray;

	// Custom
	ScriptC_grayscale scriptGrayscaleManual;
	ScriptC_wavy scriptWavyManual;

	private ImageView mImg;
	private Bitmap mBmp;

	private RenderScript mRS;

	int screenHeight;
	int screenWidth;

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);

		mRS = RenderScript.create(activity);

		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		Display screen = wm.getDefaultDisplay();
		screenWidth = screen.getWidth();
		screenHeight = screen.getHeight();

		initScripts();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_image_filters,
				container, false);

		mImg = (ImageView) view.findViewById(R.id.img_photo);

		reset();

		LinearLayout linButtons = (LinearLayout) view
				.findViewById(R.id.lin_buttons_row1);
		for (int i = 0; i < linButtons.getChildCount(); i++) {
			linButtons.getChildAt(i).setOnClickListener(this);
		}

		LinearLayout linButtons2 = (LinearLayout) view
				.findViewById(R.id.lin_buttons_row2);
		for (int i = 0; i < linButtons2.getChildCount(); i++) {
			linButtons2.getChildAt(i).setOnClickListener(this);
		}

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_image_filters, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_reset:
			reset();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void initScripts() {
		Resources res = getResources();

		// Blur
		scriptBlur = ScriptIntrinsicBlur.create(mRS, Element.U8_4(mRS));
		scriptBlur.setRadius(25.f);

		// Convolve
		float corners = 0f;
		float sides = -1f;

		scriptConvolve = ScriptIntrinsicConvolve3x3.create(mRS,
				Element.U8_4(mRS));
		float f[] = new float[9];

		f[0] = corners;
		f[1] = sides;
		f[2] = corners;
		f[3] = sides;
		f[4] = 4f;
		f[5] = sides;
		f[6] = corners;
		f[7] = sides;
		f[8] = corners;
		scriptConvolve.setCoefficients(f);

		// Grayscale Intrinsic
		scriptGray = ScriptIntrinsicColorMatrix.create(mRS, Element.U8_4(mRS));

		// Grayscale Custom
		scriptGrayscaleManual = new ScriptC_grayscale(mRS, res, R.raw.grayscale);
		// scriptGrayscaleManual.set_script(scriptGrayscaleManual);

		// Wavy
		scriptWavyManual = new ScriptC_wavy(mRS, res, R.raw.wavy);
	}

	public void scaleBitmapToScreen() {
		float sW = mBmp.getWidth();
		float sH = mBmp.getHeight();

		Log.v("blarg", String.format("Screen wh %s,%s pic wh %s, %s",
				screenWidth, screenHeight, sW, sH));

		float widthRatio = screenWidth / sW;
		float heightRatio = screenHeight / sH;

		float ratio = widthRatio > heightRatio ? widthRatio : heightRatio;

		Log.v("blarg", "ratio is " + ratio);

		int scaledWidth = (int) (ratio * sW);
		int scaledHeight = (int) (ratio * sH);

		// mBmp = Bitmap.createScaledBitmap(mBmp, (int) (ratio * sW), (int)
		// (ratio * sH), false);
		int marginLeft = Math.max(0, (scaledWidth - scaledWidth) / 2);
		int marginTop = Math.max(0, (scaledHeight - screenHeight) / 2);
		mBmp = Bitmap.createScaledBitmap(mBmp, scaledWidth, (int) scaledHeight,
				false);
		mBmp = Bitmap.createBitmap(mBmp, marginLeft, marginTop, screenWidth,
				screenHeight);
		Log.v("blarg", String.format(" after Screen wh %s,%s pic wh %s, %s",
				screenWidth, screenHeight, mBmp.getWidth(), mBmp.getHeight()));

	}

	public void reset() {
		Resources res = getResources();

		BitmapFactory.Options options = new BitmapFactory.Options();
		mBmp = BitmapFactory.decodeResource(res, R.drawable.scene, options);

		Log.v("blarg",
				"bmp size before is " + mBmp.getRowBytes() * mBmp.getHeight());

		scaleBitmapToScreen();
		Log.v("blarg",
				"bmp size after is " + mBmp.getRowBytes() * mBmp.getHeight());

		mImg.setImageBitmap(mBmp);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_gauss) {
			(new BlurTask()).execute(mBmp);
		} else if (id == R.id.btn_convolve) {
			(new ConvolveTask()).execute(mBmp);
		} else if (id == R.id.btn_gray) {
			(new GrayTask()).execute(mBmp);
		} else if (id == R.id.btn_gray_rs) {
			(new GrayManualTask()).execute(mBmp);
		} else if (id == R.id.btn_wavy) {
			(new WavyManualTask()).execute(mBmp);
		}
	}

	private abstract class FilterTask extends AsyncTask<Bitmap, Void, Bitmap> {
		protected Bitmap doInBackground(Bitmap... bmps) {
			Bitmap bmp = bmps[0];

			Allocation tmpIn = Allocation.createFromBitmap(mRS, bmp);
			Allocation tmpOut = Allocation.createFromBitmap(mRS, bmp);
			applyFilter(tmpIn, tmpOut);
			tmpOut.copyTo(bmp);
			return bmp;
		}

		abstract void applyFilter(Allocation tmpIn, Allocation tmpOut);

		protected void onPostExecute(Bitmap bmp) {
			mImg.setImageBitmap(bmp);
		}
	}

	private class BlurTask extends FilterTask {
		@Override
		void applyFilter(Allocation tmpIn, Allocation tmpOut) {
			scriptBlur.setInput(tmpIn);
			scriptBlur.forEach(tmpOut);
		}
	}

	private class ConvolveTask extends FilterTask {
		@Override
		void applyFilter(Allocation tmpIn, Allocation tmpOut) {
			scriptConvolve.setInput(tmpIn);
			scriptConvolve.forEach(tmpOut);
		}
	}

	private class GrayTask extends FilterTask {
		@Override
		void applyFilter(Allocation tmpIn, Allocation tmpOut) {
			scriptGray.setGreyscale();
			scriptGray.forEach(tmpIn, tmpOut);
		}
	}

	private class GrayManualTask extends FilterTask {
		@Override
		void applyFilter(Allocation tmpIn, Allocation tmpOut) {
			scriptGrayscaleManual.set_in(tmpIn);
			scriptGrayscaleManual.set_out(tmpOut);
			scriptGrayscaleManual.forEach_root(tmpIn, tmpOut);
		}
	}

	private class WavyManualTask extends FilterTask {
		@Override
		void applyFilter(Allocation tmpIn, Allocation tmpOut) {
			scriptWavyManual.set_in(tmpIn);
			scriptWavyManual.set_out(tmpOut);
			scriptWavyManual.set_script(scriptWavyManual);
			scriptWavyManual.set_height(mBmp.getHeight());
			scriptWavyManual.forEach_root(tmpIn, tmpOut);
		}
	}
}
