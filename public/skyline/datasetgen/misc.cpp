#include "misc.h"


//// sorting

int partition (double* data, int* index, int lo, int hi, int ipart, bool desc) {
	double v=data[index[ipart]];

	int i=lo, j=hi;
	while (1) {
		while ((desc && data[index[i]] > v) || (!desc && data[index[i]] < v)) {i++;}
		while ((desc && v > data[index[j]]) || (!desc && v < data[index[j]])) {j--;}
	    if (i<j) {
	  	   int temp=index[i];
		   index[i]=index[j];
		   index[j]=temp;
		   i++; j--;
	   } else return(i);
	}
}


void quickSort(double* data, int* index, int lo, int hi, bool desc) {
	if (lo>=hi) return;

	int ip = (lo+hi+1)/2;

	int part=partition (data, index, lo, hi, ip, desc);
	quickSort (data, index, lo, part-1, desc);
	quickSort (data, index, part, hi, desc);
}

bool fuzzyLessOrEqual(double a, double b) {
	if(a<b) return true;
	if( ((b-a)<0.00001) && ( (b-a)>(0-0.00001) ) ) return true;
	return false;
}

//// sorting in place
int partitionInPlace (long* dist, int lo, int hi, int ipart, bool desc) {
	long v=dist[ipart];

	int i=lo, j=hi;
	while (1) {
		if (desc) {
			while (dist[i]>v) i++;
			while (dist[j]<v) j--;
		} else {
			while (dist[i]<v) i++;
			while (dist[j]>v) j--;
		}
	   if (i<j) {
	  	  long temp=dist[i];
		  dist[i]=dist[j];
		  dist[j]=temp;
		  i++; j--;
	   } else return(i);
	}
}


void quickSortInPlace(long* dist, int lo, int hi, bool desc) {
	if (lo>=hi) return;

	//int ip=(int) (lo+Random()*(hi-lo+1));
	int ip = (lo+hi+1)/2;

	int part=partitionInPlace (dist, lo, hi, ip, desc);
	quickSortInPlace (dist, lo, part-1, desc);
	quickSortInPlace (dist, part, hi, desc);
}

// gaussian data generation

double gauss() { // returns a gaussian random variable N(0,1)
	static int iset=0;
	static double gset;
	double fac,rsq,v1,v2;

	if (iset==0) {
		do {
			v1=2.0*Random()-1.0;
			v2=2.0*Random()-1.0;
			rsq=v1*v1+v2*v2;
		} while (rsq >= 1.0 || rsq == 0.0);
		fac=sqrt (-2.7*log(rsq)/rsq);
		gset=v1*fac;
		iset=1;
		return v2*fac;
	} else {
		iset=0;
		return gset;
	}
}




//***********************************************************************
//
//*   Lagged Fibonnacci random number generator
//
//   Paul Coddington, April 1993.
//
//
// General lagged Fibonnacci generator using subtraction, with lags
// p and q, i.e. F(p,q,-) in Marsaglia's notation.
//
// The random numbers X_{i} are obtained from the sequence:
//
//    X_{i} = X_{i-q} - X_{i-p}   mod M
//
// where M is 1 if the X's are taken to be floating point doubles in [0,1),
// as they are here.
//
// For good results, the biggest lag should be at least 1000, and probably
// on the order of 10000.
//
// The following lags give the maximal period of the generator, which is
// (2^{p} - 1)2^{n-1} on integers mod 2^n or doubles with n bits in the
// mantissa (see Knuth, or W. Zerler, Information and Control, 15 (1969) 67,
// for a complete more list).
//
//     P     Q
//   9689   471
//   4423  1393
//   2281   715
//   1279   418
//    607   273
//    521   168
//    127    63
//
// This program is based on the implementation of RANMAR in
// F. James, "A Review of Pseudo-random Number Generators",
// Comput. Phys. Comm. 60, 329 (1990).
//
// For more details, see:
//
// D.E. Knuth, The Art of Computer Programming Vol. 2:
// Seminumerical Methods, (Addison-Wesley, Reading, Mass., 1981).
//
// P. L'Ecuyer, Random numbers for simulation, Comm. ACM 33:10, 85 (1990).
//
// G.A. Marsaglia, A current view of random number generators,
// in Computational Science and Statistics: The Interface,
// ed. L. Balliard (Elsevier, Amsterdam, 1985).
//
//**********************************************************************

#define MAXSEED 900000000

#define SIGBITS 24   // Number of significant bits

// Lags
#define P_r 1279
#define Q_r  418

// Variables for lagged Fibonacci
double u[P_r+1];  // seed table
int pt0, pt1;     // pointers into the seed table

double u_2[P_r+1];
int pt0_2, pt1_2;


void saveSeed() {
	for (int i=0; i<P_r+1; i++) u_2[i]=u[i];
	pt0_2=pt0; pt1_2=pt1;
}

void restoreSeed() {
	for (int i=0; i<P_r+1; i++) u[i]=u_2[i];
	pt0=pt0_2; pt1=pt1_2;
}



//***************************************************************************
//	Initialize the random number generator.
//        Taken from RMARIN in James's review -- initializes
//	every significant bit using a combination linear
//	congruential and small lag Fibonacci generator.
//*************************************************************************

void
RandomSeed(long seed)
{
      int ij, kl, i, j, k, l;
      int ii, jj, m;
      double t, s;

      if (seed < 0) seed = - seed;
      seed = seed % MAXSEED;

      ij = seed / 30082;
      kl = seed - 30082 * ij;
      i = ((ij/177)% 177) + 2;
      j =  ij%177 + 2;
      k = ((kl/169)% 178) + 1;
      l =  kl%169;

      for ( ii = 1 ; ii <= P_r; ii++ ){
        s = 0.0;
        t = 0.5;
        for ( jj = 1 ; jj <= SIGBITS; jj++ ){
          m = (((i*j)% 179)*k)% 179;
          i = j;
          j = k;
          k = m;
          l = (53*l+1) % 169;
          if ( ((l*m)%64) >= 32)
            s = s + t;
          t = 0.5 * t;
        }
        u[ii] = s;
      }
      pt0 = P_r;
      pt1 = Q_r;

      return;
}

//***************************************************************************
//	Return a random double in [0.0, 1.0]
//***************************************************************************
double Random()
{
      double uni;

      uni = u[pt0] - u[pt1];
      if (uni < 0.0) uni = uni + 1.0;
      u[pt0] = uni;
      pt0 = pt0 - 1;
      if (pt0 == 0) pt0 = P_r;
      pt1 = pt1 - 1;
      if (pt1 == 0) pt1 = P_r;

      return(uni);
}


void SetSeed(long seed)
{
  if (seed==0) RandomSeed(time(NULL));
  else RandomSeed(seed);
}


