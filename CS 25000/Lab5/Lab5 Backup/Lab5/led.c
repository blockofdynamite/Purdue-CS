#include <stdio.h>

#include <wiringPi.h>

/* Set Pin Numbers */

#define RED_BUTTON 24

#define GREEN_BUTTON 25

#define BLUE_BUTTON 27

#define INCREASE_BRIGHTNESS 23

#define DECREASE_BRIGHTNESS 2

#define BLUE_LED 28

#define RED_LED 29

#define GREEN_LED 0

int main(void) {

/* Initialize GPIO pins to map to wiringPi numbers */

	if(wiringPiSetup() == -1)

		return 1; //return with error status (initialization failed)

	/* Set modes for the GPIO pins */

	pinMode(BLUE_LED, OUTPUT);
	pinMode(RED_LED, OUTPUT);
	pinMode(GREEN_LED, OUTPUT);
	pinMode(RED_BUTTON, INPUT);
	pinMode(BLUE_BUTTON, INPUT);
	pinMode(GREEN_BUTTON, INPUT);
	pinMode(INCREASE_BRIGHTNESS, INPUT);
	pinMode(DECREASE_BRIGHTNESS, INPUT);

	int rVal = 0;
	int bVal = 0;
	int gVal = 0;
	int iVal = 0;
	int dVal = 0;

	int rTotal = 0;
	int bTotal = 0;
	int gTotal = 0;

	softPwmCreate(BLUE_LED, 0, 100);
	softPwmCreate(RED_LED, 0, 100);
	softPwmCreate(GREEN_LED, 0, 100);

	while(1) {

		rVal = digitalRead(RED_BUTTON);
		bVal = digitalRead(BLUE_BUTTON);
		gVal = digitalRead(GREEN_BUTTON);
		iVal = digitalRead(INCREASE_BRIGHTNESS);
		dVal = digitalRead(DECREASE_BRIGHTNESS);

		if (rVal && iVal && rTotal < 100) 
			rTotal += 5;
		if (rVal && dVal && rTotal > 0)
			rTotal -= 5;

		if (bVal && iVal && bTotal < 100) 
			bTotal += 5;
		if (bVal && dVal && bTotal > 0)
			bTotal -= 5;

		if (gVal && iVal && gTotal < 100) 
			gTotal += 5;
		if (gVal && dVal && gTotal > 0)
			gTotal -= 5;

		softPwmWrite(RED_LED, rTotal);
		softPwmWrite(GREEN_LED, gTotal);
		softPwmWrite(BLUE_LED, bTotal);

		printf("g: %d, r: %d, b: %d, i: %d, d: %d, rTot: %d, gTot: %d, bTot: %d\n", rVal, gVal, bVal, iVal, dVal, rTotal, gTotal, bTotal);

		delay(100);
	}

	return 0;

}

