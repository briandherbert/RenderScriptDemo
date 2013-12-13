#pragma version(1)
#pragma rs java_package_name(ca.tutortutor.grayscale)

// The histogram script requires a number of buffers
// The source and destination images.
rs_allocation gSrc;
rs_allocation gDest;
// A buffer for the intermediate sums
// Integer values, [256][steps] in size
rs_allocation gSums;
// Final sum buffer, Integer, [256] cells in size
rs_allocation gSum;

// The height and width of the image
int gWidth;
int gHeight;
// The step, which is the number of lines processed by each thread
int gStep;
// The number of steps in total, roughly height / step
int gSteps;

// The start of the first kernel
void __attribute__((kernel)) pass1(int in, uint x, uint y) {
	// Note that x and y will indicate the coordinates of the pixel being processed
	// This kernel will be run on a range of x = [0] and y = [0 to steps]
	// Clear our output buffer for this thread.
	for (int i = 0; i < 256; i++) {
		// Set the value at i,y to 0
		rsSetElementAt_int(gSums, 0, i, y);
	}

	if (x==0 && y== 0) {
	rsDebug("gHeight ", gHeight);
	rsDebug("gWidth ", gWidth);
	rsDebug("gStep ", gStep);

	}


	// Iterate over our image
	for (int i = 0; i < gStep; i++) {
		int py = y * gStep + i; // Compute the y coordinate in the image
		if (py >= gHeight)
			return; // Might be out of range if this is the last step
		// Walk one scanline
		for (int px = 0; px < gWidth; px++) {
			// Get the pixel and convert to luminance
			uchar4 c = rsGetElementAt_uchar4(gSrc, px, py);
			int lum = (77 * c.r + 150 * c.g + 29 * c.b) >> 8;
			// Add one to the bucked for this luminance value
			int old = rsGetElementAt_int(gSums, lum, y);
			rsSetElementAt_int(gSums, old + 1, lum, y);
		}
	}
}

// This kernel is run on the Sum allocation, so its called once
// for each of the 256 levels
// Note, this is a 1D kernel
int __attribute__((kernel)) pass2(uint x) {
	int sum = 0;
	// For this level, add in the sum from each of the
	// separate partial sums
	for (int i = 0; i < gSteps; i++) {
		sum += rsGetElementAt_int(gSums, x, i);
	}
	// Return the sum for this level. Since this is a kernel
	// return value, it will automatically be placed in the allocation.

	return sum;
}

// This is an invokable function. It will be called single threaded
void rescale() {
	// Find our largest bucket value
	int maxv = 0;
	for (int i = 0; i < 256; i++) {
		maxv = max(maxv, rsGetElementAt_int(gSum, i));
	}
	// Compute the rescale value to to convert bucket values into bar heights.
	float overMax = (1.f / maxv) * gHeight;

	rsDebug("gHeight ", gHeight);
	for (int i = 0; i < 256; i++) {
		int t = rsGetElementAt_int(gSum, i);
		t = gHeight - (overMax * rsGetElementAt_int(gSum, i));
		t = max(0, t);

		rsDebug("bar ", i);
		rsDebug("barval ", t);

		rsSetElementAt_int(gSum, t, i);
	}
}

void __attribute__((kernel)) drawhis(uchar4 in, uint32_t x, uint32_t y) {

	in.r = 255 - in.r;
	in.g = 255 - in.g;
	in.b = 255 - in.b;
}

void drawhist(const uchar4* v_in, uchar4* v_out, const void* usrData, uint x, uint y) {
	int barWidth = gWidth / 256;
	int bar = x / barWidth;
	int val = min(255, (int) y);

	int histVal = (int) rsGetElementAt_int(gSum, bar);

	if (y == 0) {
//		rsDebug("bar ", bar);
//		rsDebug("barval ", histVal);

	}

	if (bar < 256 && y > (int) rsGetElementAt_int(gSum, bar)) {
		(*v_out).r = 0;
		(*v_out).g = 0;
		(*v_out).b = 0;
	}

	if (y % 100 == 0) {
		(*v_out).r = 0;
		(*v_out).g = 255;
		(*v_out).b = 0;
	}

	if (bar > 256) {
		(*v_out).r = 255;
		(*v_out).g = 0;
		(*v_out).b = 0;
	}

}

void invert(const uchar4* v_in, uchar4* v_out, const void* usrData, uint32_t x,
		uint32_t y) {

	(*v_out).r = 255 - (*v_in).r;
	(*v_out).g = 255 - (*v_in).g;
	(*v_out).b = 255 - (*v_in).b;
}

void drawxy(const uchar4* v_in, uchar4* v_out, const void* usrData, uint x,
		uint y) {
	int val = min(255, (int) y);
//	rsDebug("x = ", (int)x);
//	rsDebug("y = ", (int)y);

	if (x == 100 || y == 100) {
		(*v_out).r = 255;
		(*v_out).g = 0;
		(*v_out).b = 0;
	} else {
	(*v_out).r = val;
	(*v_out).g = val;
	(*v_out).b = val;
	}
}

