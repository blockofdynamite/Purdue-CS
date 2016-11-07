#include <stdio.h>

#include <wiringPi.h>

/* Set Pin Numbers */

#define LED 28

#define BUTTON 29

#define RED_BUTTON 35

#define GREEN_BUTTON 37

#define BLUE_BUTTON 31

#define INCREASE_BRIGHTNESS 33

#define DECREASE_BRIGHTNESS 13

#define BLUE_LED 38

#define RED_LED 40

#define GREEN_LED 11

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
		if (rVal && dVal && rTotal > 100)
			rTotal -= 5;

		if (bVal && iVal && rTotal < 100) 
			bTotal += 5;
		if (bVal && dVal && rTotal > 100)
			bTotal -= 5;

		if (gVal && iVal && rTotal < 100) 
			gTotal += 5;
		if (gVal && dVal && rTotal > 100)
			gTotal -= 5;

		softPwmWrite(RED_LED, rTotal);
		softPwmWrite(GREEN_LED, gTotal);
		softPwmWrite(BLUE_LED, bTotal);
		
		delay(100);
	}

	return 0;

}

