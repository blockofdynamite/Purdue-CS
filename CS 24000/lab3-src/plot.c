
#include <stdio.h>
#include <stdlib.h>

#include "rpn.h"

#define MAXCOLS 80
#define MAXROWS 40

char plot[MAXROWS][MAXCOLS];

void clearPlot() {
  for (int i = 0; i < MAXROWS; i++) {
    for (int j = 0; j < MAXCOLS; j++) {
      plot[i][j] = ' ';
    }
  }
}

void printPlot() {
  for (int i = 0; i < MAXROWS; i++) {
    for (int j = 0; j < MAXCOLS; j++) {
      printf("%c", plot[i][j]);
    }
    printf("\n");
  }
}

void plotXY(int x, int y, char c) {
  if ( x < 0 || x >= MAXCOLS || y < 0 || y >= MAXROWS) return;
  plot[y][x]=c;
}

void createPlot( char * funcFile, double minX, double maxX) {
  int nvals = MAXCOLS;
  double yy[MAXCOLS];

  clearPlot();

  int width = maxX - minX;

  double includedXVals = (maxX - minX) / MAXCOLS;

  // Evaluate function and store in vector yy
  for (int i = 0; i < MAXCOLS; i++) {
    yy[i] = rpn_eval(funcFile, i * includedXVals + minX);
  }

  //Compute maximum and minimum y in vector yy
  double maxY = 0;
  double minY = 0;
  for (int i = 0; i < MAXCOLS; i++) {
    if (i == 0) {
        maxY = yy[i];
        minY = yy[i];
    }
    else if (yy[i] > maxY) {
        maxY = yy[i];
    } else if (yy[i] < minY) {
        minY = yy[i];
    }
  }
  
  //Plot x axis
  double change = (maxY - minY) / MAXROWS;

  double y = maxY;
  for (int i = 0; i < MAXROWS; i++) {
    y -= change;
    //printf("%lf\n", y);
    if (y >= 0 && y <= change) {
        for (int j = 0; j < MAXCOLS; j++) {
            plotXY(j, i + 1, '_'); 
        }
        //plotXY(0, y, '_'); 
        //printf("I was here!");
        break;
    }
  }

  double changeX = (maxX - minX) / MAXCOLS;
  double x = maxX;
  for (int i = 0; i < MAXCOLS; i++) {
    x -= changeX;
    //printf("%lf\n", x);
    if (x >= 0 && x <= changeX) {
        for (int j = 0; j < MAXCOLS; j++) {
            plotXY(i + 1, j, '|'); 
        }
        //plotXY(0, y, '_'); 
        //printf("I was here!");
        break;
    }
  }

  for (int i = 0; i < MAXCOLS; i++) {
  	int yCoord = ((yy[i] - minY) * MAXROWS) / (maxY - minY);
  	//printf("%lf\n", yy[i]);
  	//printf("%d\n", yCoord);
		plotXY(i, MAXROWS - yCoord - 1, '*');
  }
  // minX is plotted at column 0 and maxX is plotted at MAXCOLS-1
	
  // minY is plotted at row 0 and maxY is plotted at MAXROWS-1

  printPlot();

}

int main(int argc, char ** argv) {
  printf("RPN Plotter.\n");
  
  if (argc < 4) {
    printf("Usage: plot func-file xmin xmax\n");
    exit(1);
  }

  double xmin = atof(argv[2]);
  double xmax = atof(argv[3]);
  
  //createPlot(funcName, xmin, xmax);
  createPlot(argv[1], xmin, xmax);
}
