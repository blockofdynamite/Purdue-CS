#include "stdafx.h"
#include "Speedup.h"


Speedup::Speedup()
{
}


Speedup::~Speedup()
{
}

WaveFile * Speedup::getEffect(WaveFile * wave, int start, int end)
{
	return wave->multiply_freq(2, start, end);
}
