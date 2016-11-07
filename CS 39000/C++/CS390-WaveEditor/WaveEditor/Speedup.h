#pragma once
#include "Filter.h"
class Speedup : public Filter
{
public:
	Speedup();
	~Speedup();
	static WaveFile * getEffect(WaveFile * wave, int start, int end);
};

