#include "stdafx.h"
#include "FilterManager.h"
#include "Speedup.h"
#include "Slowdown.h"
#include "Echo.h"

FilterManager::FilterManager()
{
}


FilterManager::~FilterManager()
{
}

WaveFile * FilterManager::getSpeedUp(WaveFile * wave, int start, int end)
{
	return Speedup::getEffect(wave, start, end);
}

WaveFile * FilterManager::getSlowDown(WaveFile * wave, int start, int end) 
{
	return Slowdown::getEffect(wave, start, end);
}

WaveFile * FilterManager::getEcho(WaveFile * wave, int start, int end) 
{
	return Echo::getEffect(wave, start, end);
}
