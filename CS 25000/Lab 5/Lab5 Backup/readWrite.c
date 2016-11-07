#include <stdio.h>

#include <wiringPi.h>

#define LED 28

#define BUTTON 29

int main(char argc, char** argv) {
	if (wiringPiSetup() == -1)
		return 1;

	pinMode(LED, OUTPUT);
	pinMode(BUTTON, INPUT);

	int value = LOW;

	while (1) {
		value = digitalRead(BUTTON);
		digitalWrite(LED, value);
		
		delay(100);
	}

	return 0;
}
