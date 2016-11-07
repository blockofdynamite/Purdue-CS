#pragma once
#include "Filter.h"
class Slowdown :
	public Filter
{
public:
	Slowdown();
	~Slowdown();
	static WaveFile * getEffect(WaveFile * wave, int start, int end);
};

