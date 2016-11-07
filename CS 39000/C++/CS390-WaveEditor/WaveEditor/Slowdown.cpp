#include "stdafx.h"
#include "Slowdown.h"


Slowdown::Slowdown()
{
}


Slowdown::~Slowdown()
{
}

WaveFile * Slowdown::getEffect(WaveFile * wave, int start, int end)
{
	return wave->multiply_freq(0.5, start, end);
}
