#pragma once
#include "Filter.h"
class Echo :
	public Filter
{
public:
	Echo();
	~Echo();
	static WaveFile * getEffect(WaveFile * wave, int start, int end);
};

