#pragma once

#include "WaveFile.h"

class Filter
{
public:
	Filter();
	~Filter();
	static WaveFile * getEffect(WaveFile * wave, int start, int end);
};

