package com.example.renderscriptdemo.fragment;

import ca.tutortutor.grayscale.ScriptC_grayscale;
import ca.tutortutor.grayscale.ScriptC_histogram;
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
	ScriptC_histogram scriptHistogram;

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
		
		initScripts();

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

		// Wavy
		scriptWavyManual = new ScriptC_wavy(mRS, res, R.raw.wavy);
		
		// Histogram
		scriptHistogram = new ScriptC_histogram(mRS);
		scriptHistogram.set_gWidth(mBmp.getWidth());
		scriptHistogram.set_gHeight(mBmp.getHeight());
		
		
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
		mBmp = BitmapFactory.decodeResource(res, R.drawable.dalmation, options);

		scaleBitmapToScreen();

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
		} else if (id == R.id.btn_histogram) {
			(new HistogramTask()).execute(mBmp);
		}
	}

	private abstract class FilterTask extends AsyncTask<Bitmap, Void, Bitmap> {
		protected Bitmap doInBackground(Bitmap... bmps) {
			Bitmap bmp = bmps[0];

			Allocation alloc = Allocation.createFromBitmap(mRS, bmp);
			//Allocation tmpOut = Allocation.createFromBitmap(mRS, bmp);
			applyFilter(alloc, alloc);
			alloc.copyTo(bmp);
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
			scriptGrayscaleManual.set_script(scriptGrayscaleManual);
			//scriptGrayscaleManual.forEach_root(tmpIn, tmpOut);
			scriptGrayscaleManual.invoke_filter();
		}
	}

	private class WavyManualTask extends FilterTask {
		@Override
		void applyFilter(Allocation tmpIn, Allocation tmpOut) {
			scriptWavyManual.set_in(tmpIn);
			scriptWavyManual.set_out(tmpOut);
			scriptWavyManual.set_script(scriptWavyManual);
			scriptWavyManual.set_height(mBmp.getHeight());
			//scriptWavyManual.forEach_root(tmpIn, tmpOut);
			scriptWavyManual.invoke_filter();
		}
	}
	
	private class HistogramTask extends FilterTask {
		int step = 50;
		int steps = (int) Math.ceil(mBmp.getHeight() / (double)step);
		
		@Override
		void applyFilter(Allocation tmpIn, Allocation tmpOut) {
			scriptHistogram.set_gSrc(tmpIn);
			scriptHistogram.set_gStep(step);
			scriptHistogram.set_gSteps(steps);

			
			// Create the [256][steps] buffer of integers for the partial sums
			Type.Builder tb = new Type.Builder(mRS, Element.I32(mRS));
			tb.setX(256).setY(steps);
			Type t = tb.create();
			Allocation mSums = Allocation.createTyped(mRS, t);
			// Create the 1D [256] buffer for the final Sums
			Allocation mSum = Allocation.createSized(mRS, Element.I32(mRS), 256);
			// Set the buffers for the script
			scriptHistogram.set_gSums(mSums);
			scriptHistogram.set_gSum(mSum);
			
			// This first pass should be clipped because we want [step] threads not [256][step] threads
			// To do this we have the ability to clip our launch
			// using a LaunchOptions class 
			Script.LaunchOptions lo = new Script.LaunchOptions();
			// Set the range in the X dimension to be 0 to 1
			// Note: the end is exclusive so this says only run X value 0
			lo.setX(0, 1);
			// Run the kernel with our launch options
			// This will spawn one thread per Y coordinate, each with X=0
			Log.v("blarg", "first pass");
			scriptHistogram.forEach_pass1(mSums, lo);
			
			// Once the first pass is complete, we need to add up our partial sums
			// The pass2 launch is unclipped. It spawns one thread per cell in the mSum buffer
			// for a total of 256 threads.
			Log.v("blarg", "second pass");

			scriptHistogram.forEach_pass2(mSum);
			
			// Finally, we call our rescale function
			Log.v("blarg", "rescale");

			scriptHistogram.invoke_rescale();
			
			Log.v("blarg", "histogram");

			Script.LaunchOptions lo2 = new Script.LaunchOptions();
			scriptHistogram.forEach_drawhist(tmpIn, tmpOut);
			
			Log.v("blarg", "done");

		}
		
		@Override
		protected void onPostExecute(Bitmap bmp) {
			Log.v("blarg", "setting bitmap");
			mImg.setImageBitmap(bmp);
			Log.v("blarg", " done setting bitmap");

		}
	}
	

}
