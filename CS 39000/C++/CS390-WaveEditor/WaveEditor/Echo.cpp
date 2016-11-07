#include "stdafx.h"
#include "Echo.h"


Echo::Echo()
{
}


Echo::~Echo()
{
}

WaveFile * Echo::getEffect(WaveFile * wave, int start, int end) 
{
	return wave->echo(0.5, 750, start, end);
}
