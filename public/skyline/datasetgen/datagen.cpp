#include "datagen.h"


void fillVectorUniform (double* v, int numT, double min, double max) {
	for (int i=0; i<numT; i++)
		v[i]=min + (max-min)*Random();
}


CQueryGen::CQueryGen(int numQ0, int numS, double minW, double maxW, int k) {
	numQ = numQ0;

	q = new CQuery* [numQ];
	for (int i=0; i<numQ; i++) {
		q[i] = new CQuery(numS);
		fillVectorUniform(q[i]->q, numS, 0, 1);
		fillVectorUniform(q[i]->w, numS, minW, maxW);
		q[i]->k = k;
	}

}

void CQueryGen::setk(int k) {
	for (int i=0; i<numQ; i++) {
		q[i]->k = k;
	}
}


CQueryGen::~CQueryGen() {
	for (int i=0; i<numQ; i++)
		delete q[i];
	delete[] q;
}



/////////////////////////
// Base CDataGen class
/////////////////////////

CDataGen::CDataGen(int numT0, int numS0, int numSR0) {

	numT = numT0;
	numS = numS0;
	numSR = numSR0;

	d = new double*[numT];
	for (int i=0; i<numT; i++)
		d[i] = new double[numS];

	costR = new double[numS0];
	costS = new double[numS0];
	//ts = new TSource[numS0];

	minCostS=0.1; maxCostS=1;
	minCostR=1; maxCostR=10;
	factorCostFirstR = 0;
}


CDataGen::~CDataGen() {

	for (int i=0; i<numT; i++)
		delete[] d[i];
	delete[] d;

	delete[] costR;
	delete[] costS;
	//delete[] ts;
}


void CDataGen::generate() {
	generateRest();
	generateData();
}


void CDataGen::setCostRBounds(double min, double max) {
	minCostR=min;
	maxCostR=max;
}

void CDataGen::setCostSBounds(double min, double max) {
	minCostS=min;
	maxCostS=max;
}


void CDataGen::generateRest() {
	fillVectorUniform(costR, numS, minCostR, maxCostR);
	fillVectorUniform(costS, numS, minCostS, maxCostS);
	int i;
	//for (i=0; i<numS; i++) ts[i]= i<numSR?__SR:__R;
	if (factorCostFirstR!=0) {
		costR[1]=costR[0];
		for (i=2; i<numS; i++) costR[1]+=costR[i];
		costR[1] = costR[1]/(numS-1)*factorCostFirstR;
	}
}



std::ostream& operator << (std::ostream& o, CDataGen* d) {

	if (!d) o << "[NULL]";
	else {
		for (int j=0; j<d->numS; j++) {
			char buffer[64];
			o << "\n Source " <<j<< ")\n Size=" << d->numT << " ";

			o << " \n[ ";
			for (int i=0; i<d->numT; i++) {
				sprintf (buffer, "%2.4f", d->d[i][j]);
				o << buffer << " ";
			}

			o << "]\n";

		}
	}

	return o;
}


////////////////////////////
/// Uniform
////////////////////////////


CDataGenUniform::CDataGenUniform(int numT, int numS, int numSR):CDataGen(numT, numS, numSR) {

	scoreMin = new double[numS];
	scoreMax = new double[numS];

	for (int i=0; i<numS; i++) {
		scoreMin[i]=0;
		scoreMax[i]=1;
	}
}


CDataGenUniform::~CDataGenUniform() {

	delete[] scoreMin;
	delete[] scoreMax;

}


void CDataGenUniform::generateData () {

	for (int j=0; j<numS; j++)
		generateDataColumn(j);

}



void CDataGenUniform::generateDataColumn (int col) {

	for (int i=0; i<numT; i++)
		d[i][col]=scoreMin[col] + (scoreMax[col]-scoreMin[col])*Random();

}



/////////////////////////////////
//// Correlated
/////////////////////////////////


void CDataGenCorrelated::generateDataColumnCorrelated (int col, int correlatedTo, double correlationInterval) {

	for (int i=0; i<numT; i++) {
		double center=d[i][correlatedTo];
		double lo = MAX(0, center-(1-fabs(correlationInterval)));
		double hi = MIN(1, center+(1-fabs(correlationInterval)));
		double value = lo + Random()*(hi-lo);
		if (correlationInterval<0) value=1-value;
		d[i][col]=value;
	}

}



CDataGenCorrelated::CDataGenCorrelated(int numT, int numS, int numSR, double sigma0)
	:CDataGenUniform(numT, numS, numSR) {
		sigma = sigma0;
		sortedIsUncorrelated = false;
}

void CDataGenCorrelated::generateData() {

	generateDataColumn(0);

	for (int j=1; j<numS; j++)
		generateDataColumnCorrelated(j, 0, sigma);

	if (sortedIsUncorrelated) // generate first column again, so it becomes uncorrelated
		generateDataColumn(0);

}


/////////////////////////////////
//// Partial Correlated
/////////////////////////////////


void CDataGenPartialCorrelated::generateData() {

	generateDataColumn(0);

	int j;

	for (j=1; j<col; j++)
		generateDataColumnCorrelated(j, 0, sigma);

	for (j=col; j<numS; j++)
		generateDataColumnCorrelated(j, 0, -sigma);
}

void CDataGenPartialCorrelated2::generateData() {

	generateDataColumn(numS-1);

	int j;

	for (j=0; j<col-1; j++)
		generateDataColumnCorrelated(j, numS-1, sigma);

	for (j=col-1; j<numS-1; j++)
		generateDataColumnCorrelated(j, numS-1, -sigma);
}



////////////////////////////
/// Gaussian
////////////////////////////


long* getZipfianList(double z, int N, int distinctValues) {
	//   z: zipfian parameter
	//   N: total number of tuples
	//   distinctValues:total number of distinct tuples
	//   fi = N / ( i^z * K )  where K = sum_{j=1}^distinctValues (1/j^z) )

	long* f = new long[distinctValues];

	// get K
	double K = 0;
	int i;
	for (i=1; i<=distinctValues; i++)
		K += 1/pow(i,z);

	double rem = 0.0001;
	int lastWithMoreThanOneTuple = 0;
	for (i=0; i<distinctValues; i++) {
		//if (i%100==0) cout << "Generating zipfian list... " << i << "/" << distinctValues << "\t\t\t\t\r" << flush;
		double fi = (double)N/ (pow(i+1,z)*K);
		f[i] = long(fi);
		rem += fi-f[i];
		// if we accumulated a whole number, add it
		if (rem>=1) {
			rem-=1;
			f[i] += 1;
		}

		if (f[i]>1) lastWithMoreThanOneTuple = i;

		if (f[i]==0) { // we don't want zeroes, so we steal one from the last element with more than one tuple
			f[i]=1;

			f[lastWithMoreThanOneTuple]--;
			while (f[lastWithMoreThanOneTuple]==1)
				lastWithMoreThanOneTuple--;
			assert (lastWithMoreThanOneTuple>=0);
		}
	}

//	cout << "Done...Sorting...\t\t\t\t\t\r" << flush;
	quickSortInPlace(f, 0, distinctValues-1, true);
//	cout << "Done\t\t\t\t\t\r" << flush;

	return(f);
}



CDataGaussian::CDataGaussian(int numT, int numS, int numSR, double z0, int peaks0, double sigma0)
	:CDataGen(numT, numS, numSR) {
		z=z0; peaks=peaks0; sigma=sigma0;
}


void CDataGaussian::generateData () {

	// get frequencies.
	long* freqs = getZipfianList(z, numT, peaks);

	int i, j, k, act=0;

	double* center = new double[numS];

	for (i=0; i<peaks; i++) {
		for (k=0; k<numS; k++)
			center[k]=Random();

		for (j=0; j<freqs[i]; j++) {

			for (k=0; k<numS; k++) {
				double value;
				do {
					value=center[k]+sigma*gauss();
				} while (value<0 || value>1);
				d[act][k]=value;
			}
			act++;
		}
	}

	delete[] freqs;
	delete[] center;

}

