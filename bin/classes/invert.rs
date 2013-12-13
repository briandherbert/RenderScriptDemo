#pragma version(1)
#pragma rs java_package_name(com.meetme.bherbert)

void invert(const uchar4* v_in, uchar4* v_out, const void* usrData, uint32_t x,
		uint32_t y) {

	(*v_out).r = 255 - (*v_in).r;
	(*v_out).g = 255 - (*v_in).g;
	(*v_out).b = 255 - (*v_in).b;
}
