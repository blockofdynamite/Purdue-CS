//
// Simple Library for Wave File Manipulation.
//
// Copyright Gustavo Rodriguez-Rivera grr@cs.purdue.edu
//
// Use this code at your own risk.
// You are free to copy this code in any application 
// free or commercial as long as this notice remains in the sources.
//
// See https://ccrma.stanford.edu/courses/422/projects/WaveFormat/ for more
// information about the wave file format.
// 
// Version 1/24/2012 7:15am

#include "StdAfx.h"
#include "WaveFile.h"
#include "windows.h"
#include "mmsystem.h"
#include "assert.h"
#include "math.h"

static void
assignLittleEndian4(unsigned char * dest, unsigned int value) {
	dest[0] = value & 0xFF;
	dest[1] = (value >> 8) & 0xFF;
	dest[2] = (value >> 16) & 0xFF;
	dest[3] = (value >> 24) & 0xFF;
}

static int
getLittleEndian4(unsigned char * src) {
	return     src[0] + (src[1] << 8) + (src[2] << 16) + (src[3] << 24);
}

static int
isLittleEndian() {
	static int littleEndian;
	static int initialized;

	if (initialized) {
		return littleEndian;
	}

	littleEndian = 0;

	// Check if machine is big endian or little endian
	int test = 5;
	if (*(char*)&test == 5) {
		littleEndian = 1;
	}

	initialized = 1;
}

// Create an empty wave file.
WaveFile::WaveFile(void)
{
	hdr = NULL;
}

// Create an empty wave file with these parameters.
WaveFile::WaveFile(int numChannels, int sampleRate, int bitsPerSample) {
	this->numChannels = numChannels;
	this->sampleRate = sampleRate;
	this->bitsPerSample = bitsPerSample;
	this->bytesPerSample = numChannels * bitsPerSample / 8;

	this->lastSample = 0;
	this->maxSamples = 100000;

	hdr = (WaveHeader *)
		malloc(sizeof(WaveHeader) - 1 + maxSamples * bytesPerSample);
}

// Destructor
WaveFile::~WaveFile(void)
{

}

// Read a wave file from opoened file f
bool
WaveFile::read(CFile * f)
{
	//FILE * f = fopen(fileName, "r");

	if (f == NULL) {
		return false;
	}

	int fsize = f->GetLength();

	if (fsize < sizeof(WaveHeader)) {
		return false;
	}

	// Allocate memory for wave file
	hdr = (WaveHeader *)malloc(fsize + 1);

	// Read file
	f->Read(hdr, fsize);

	// Validate that this is 16bit wav file with no compression
	if (
		hdr->chunkID[0] != 'R' || hdr->chunkID[1] != 'I' ||
		hdr->chunkID[2] != 'F' || hdr->chunkID[3] != 'F' ||
		hdr->format[0] != 'W' || hdr->format[1] != 'A' ||
		hdr->format[2] != 'V' || hdr->format[3] != 'E' ||
		hdr->subchunk1ID[0] != 'f' || hdr->subchunk1ID[1] != 'm' ||
		hdr->subchunk1ID[2] != 't' || hdr->subchunk1ID[3] != ' '
		)
	{
		free(hdr);
		hdr = NULL;

		return false;
	}

	// Fill it up
	numChannels = hdr->numChannels[0];
	sampleRate = getLittleEndian4(hdr->sampleRate);
	bitsPerSample = hdr->bitsPerSample[0] + (hdr->bitsPerSample[1] << 8);
	bytesPerSample = (bitsPerSample / 8)*numChannels;
	lastSample = getLittleEndian4(hdr->subchunk2Size) / bytesPerSample;
	maxSamples = lastSample;
	dataSize = lastSample * bytesPerSample;
	fileSize = sizeof(WaveHeader) - 1 + dataSize;

	// Check that the samples use 16 bits
	if (bitsPerSample != 16) {
		return false;
	}

	return true;
}

// Play wave file.
void
WaveFile::play()
{
	if (hdr == NULL) {
		return;
	}

	PlaySoundW((LPCWSTR)hdr, NULL, SND_MEMORY | SND_ASYNC | SND_NODEFAULT);
}

// Add a new sample at the end of the wave file
void
WaveFile::add_sample(int sample) {
	assert(bitsPerSample == 16);

	// Exapand umber of samples if necessary
	if (lastSample == maxSamples) {
		maxSamples *= 2;
		hdr = (WaveHeader *)realloc(hdr,
			sizeof(WaveHeader) - 1 + maxSamples * bytesPerSample);
		assert(hdr != NULL);
	}

	short value = sample;
	int i = lastSample;
	if (isLittleEndian()) {
		hdr->data[2 * i] = *((unsigned char *)&value);
		hdr->data[2 * i + 1] = *((unsigned char *)&value + 1);
	}
	else {
		hdr->data[2 * i + 1] = *((unsigned char *)&value);
		hdr->data[2 * i] = *((unsigned char *)&value + 1);
	}
	lastSample++;
}

// Get ith sample
int
WaveFile::get_sample(int i) {
	short val;
	if (isLittleEndian()) {
		*((unsigned char*)&val + 1) = (hdr->data[2 * i + 1]);
		*((unsigned char*)&val) = (hdr->data[2 * i]);
	}
	else {
		*((unsigned char*)&val) = (hdr->data[2 * i + 1]);
		*((unsigned char*)&val + 1) = (hdr->data[2 * i]);
	}
	return val;
}

// Update the wave header in memory with the latest number of samples etc.
// We need to call updateHeader before saving or playing the file after
// the wave file has been updated.
void
WaveFile::updateHeader()
{
	dataSize = lastSample * bytesPerSample;
	fileSize = sizeof(WaveHeader) - 1 + dataSize;

	// Fill up header
	hdr->chunkID[0] = 'R'; hdr->chunkID[1] = 'I'; hdr->chunkID[2] = 'F'; hdr->chunkID[3] = 'F';
	assignLittleEndian4(hdr->chunkSize, fileSize - 8);
	hdr->format[0] = 'W'; hdr->format[1] = 'A'; hdr->format[2] = 'V'; hdr->format[3] = 'E';
	hdr->subchunk1ID[0] = 'f'; hdr->subchunk1ID[1] = 'm'; hdr->subchunk1ID[2] = 't'; hdr->subchunk1ID[3] = ' ';
	assignLittleEndian4(hdr->subchunk1Size, 16);
	hdr->audioFormat[0] = 1; hdr->audioFormat[1] = 0;
	hdr->numChannels[0] = numChannels; hdr->numChannels[1] = 0;
	assignLittleEndian4(hdr->sampleRate, sampleRate);
	assignLittleEndian4(hdr->byteRate, numChannels * sampleRate * bitsPerSample / 8);
	hdr->blockAlign[0] = numChannels * bitsPerSample / 8; hdr->blockAlign[1] = 0;
	hdr->bitsPerSample[0] = bitsPerSample; hdr->bitsPerSample[1] = 0;
	hdr->subchunk2ID[0] = 'd'; hdr->subchunk2ID[1] = 'a'; hdr->subchunk2ID[2] = 't'; hdr->subchunk2ID[3] = 'a';
	assignLittleEndian4(hdr->subchunk2Size, dataSize);
}

// Save wave file in opened file f
void
WaveFile::save(CFile * f) {

	if (f == NULL) {
		return;
	}

	updateHeader();

	f->Write(hdr, fileSize);
}

// Create a tone with this frequency and this number of msecs.
void
WaveFile::tone(int frequency, int msecs) {
	// Generate data
	unsigned int numSamples = msecs * sampleRate / 1000;
	int i;
	for (i = 0; i<numSamples; i++) {
		short value = 32767 * sin(3.1415*frequency*i / sampleRate);
		add_sample(value);
	}
}

WaveFile *
WaveFile::multiply_freq(double k, int begin, int end)
{
	WaveFile * w2 = new WaveFile(numChannels, sampleRate, bitsPerSample);
	if (begin == end) {
		begin = 0;
		end = lastSample;
	}

	double i = 0;
	if (k != 0) {
		for (i = 0; i < begin; i++) {
			w2->add_sample(get_sample((int)i));
		}

		while (i < end) {
			w2->add_sample(get_sample((int)i));
			i += k;
		}

		while (i < lastSample) {
			w2->add_sample(get_sample((int)i));
			i++;
		}
	}

	w2->updateHeader();

	return w2;
}

// Append a wave file src to the end of this wave file. 
void
WaveFile::append_wave(WaveFile * src) {
	int i;
	for (i = 0; i < src->lastSample; i++) {
		int sample = src->get_sample(i);
		add_sample(sample);
	}
}

// Create a new wavefile with echo from the original one.
// echoAmount is a constant 0 to 1 with the amount of echo
// delayms is the delay of the echo added to the original.
WaveFile *
WaveFile::echo(float echoAmount, float delayms, int begin, int end)
{
	if (end == 0 || end > lastSample) {
		end = lastSample;
	}
	if (begin < 0 || begin > end) {
		begin = 0;
	}
	if (begin == end) {
		begin = 0;
		end = lastSample;
	}

	WaveFile * w2 = new WaveFile(numChannels, sampleRate, bitsPerSample);

	int i = 0;
	int t = 0;

	int delayInSamples = sampleRate * (delayms / 1000);

	if (begin == 0 && end == lastSample) {
		while (t < lastSample) {
			float value = get_sample((int)t);
			float delayed = 0;
			if (t - delayInSamples >= 0) {
				delayed = w2->get_sample((int)t - delayInSamples);
			}
			w2->add_sample((int)((1 - echoAmount)*value + echoAmount * delayed));
			t++;
		}
	}

	else {
		for (t = 0; t < begin; t++) {
			w2->add_sample(get_sample((int)t));
		}

		while (t < end + delayInSamples) {
			float value = 0;
			float delayed = 0;
			if (t < end) {
				value = get_sample((int)t);
			}
			if (t - delayInSamples >= 0) {
				delayed = get_sample((int)t - delayInSamples);
			}
			w2->add_sample((int)((1 - echoAmount)*value + echoAmount * delayed));
			t++;
		}

		while (t < lastSample) {
			w2->add_sample(get_sample((int)t));
			t++;
		}
	}

	w2->updateHeader();

	return w2;
}

WaveFile * WaveFile::get_fragment(int start, int end)
{
	WaveFile * fragment = new WaveFile(numChannels, sampleRate, bitsPerSample);

	for (int i = start; i < end; i++) {
		fragment->add_sample(get_sample(i));
	}

	fragment->updateHeader();

	return fragment;
}

WaveFile * WaveFile::insert_fragment(WaveFile * fragment, int index)
{
	WaveFile * wave = new WaveFile(numChannels, sampleRate, bitsPerSample);
	int localCounter, fragmentCounter;
	for (localCounter = 0; localCounter < index; localCounter++) {
		wave->add_sample(get_sample(localCounter));
	}
	for (fragmentCounter = 0; fragmentCounter < fragment->lastSample; fragmentCounter++) {
		wave->add_sample(fragment->get_sample(fragmentCounter));
	}
	while (localCounter < lastSample) {
		wave->add_sample(get_sample(localCounter));
		localCounter++;
	}
	wave->updateHeader();
	return wave;
}

WaveFile * WaveFile::remove_fragment(int start, int end)
{
	WaveFile * fragment = new WaveFile(numChannels, sampleRate, bitsPerSample);

	for (int i = 0; i < start; i++) {
		fragment->add_sample(get_sample(i));
	}

	for (int i = end; i < lastSample; i++) {
		fragment->add_sample(get_sample(i));
	}

	fragment->updateHeader();
	return fragment;
}
