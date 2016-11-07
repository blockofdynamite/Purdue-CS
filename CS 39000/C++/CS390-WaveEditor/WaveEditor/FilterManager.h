#pragma once
#include "WaveFile.h"

class FilterManager
{
public:
	FilterManager();
	~FilterManager();
	static WaveFile * getSpeedUp(WaveFile * wave, int start, int end);
	static WaveFile * getSlowDown(WaveFile * wave, int start, int end);
	static WaveFile * getEcho(WaveFile * wave, int start, int end);
};

