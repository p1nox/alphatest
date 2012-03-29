#ifndef __MISC_H
#define __MISC_H

#include <assert.h>
#include <ctype.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <fstream>
#include <iomanip>

#ifdef WIN32
  #include <windows.h>
  #include <mmsystem.h>
#else
  #include <limits.h>
  #include <sys/times.h>
#endif


#define SQR(a) ((a)*(a))
#define MIN(a,b) ((a)<(b)?(a):(b))
#define MAX(a,b) ((a)>(b)?(a):(b))

#define deleteSafe(x) if (x) delete x; x=NULL;
#define deleteSafeArray(x) if (x) delete[] x; x=NULL;

#define EPSILON 0.00000001


// functions
template <class T> void swapT( T& a, T& b ) { T c = a; a = b; b = c; }

int partition (double* data, int* index, int lo, int hi, int ipart, bool desc) ;

void quickSort(double* data, int* index, int lo, int hi, bool desc);
void quickSortInPlace(long* dist, int lo, int hi, bool desc);

double convert (double v, int dec);
bool fuzzyLessOrEqual(double a, double b);
double gauss();

void SetSeed(long seed=0);
double Random();
void saveSeed();
void restoreSeed();

#endif
