#ifndef __DATAGEN_H
#define __DATAGEN_H

#include "query.h"

void fillVectorUniform (double* v, int numT, double min, double max);

class CQueryGen {
public:
	int numQ;
	CQuery** q;

	CQueryGen(int numQ0, int numS, double minW, double maxW, int k);
	void setk(int k);
	~CQueryGen();
};


class CDataGen {
public:
	int numT;
	int numS;
	int numSR;
	double** d;
	double* costR;
	double* costS;
	//TSource* ts;

	double minCostS, maxCostS;
	double minCostR, maxCostR;
	double factorCostFirstR;

	CDataGen(int numT0, int numS0, int numSR0);
	~CDataGen();

	virtual void generateData() = 0;
	virtual void generateRest();
	virtual void generate();

	void setCostRBounds(double min, double max);
	void setCostSBounds(double min, double max);

	friend std::ostream& operator << (std::ostream& o, CDataGen* d);
};


class CDataGenUniform:public CDataGen {
protected:
	void generateDataColumn (int col);

public:
	double* scoreMin;  // min and max possible values for
	double* scoreMax;  // the elements (used to change the expected value!)

	CDataGenUniform(int numT, int numS, int numSR);
	~CDataGenUniform();
	void generateData();
};



class CDataGenCorrelated:public CDataGenUniform {
protected:
	void generateDataColumnCorrelated (int col, int correlatedTo, double correlationInterval);

public:
	bool sortedIsUncorrelated; // by default, it's false so the random sources are correlated to the sorted one. Otherwise, we regenerate the sorted source at the end...
	double sigma;

	CDataGenCorrelated(int numT, int numS, int numSR, double sigma0);
	void generateData();
};


class CDataGenPartialCorrelated:public CDataGenCorrelated { //correlated from sr to r (smaller group is all sr)
public:
	int col;

	CDataGenPartialCorrelated(int numT, int numS, int numSR, double sigma, int col0):CDataGenCorrelated(numT, numS, numSR, sigma) { col = col0; }
	void generateData();
};

class CDataGenPartialCorrelated2:public CDataGenCorrelated { //one r source in smaller group
public:
	int col;

	CDataGenPartialCorrelated2(int numT, int numS, int numSR, double sigma, int col0):CDataGenCorrelated(numT, numS, numSR, sigma) { col = col0; }
	void generateData();
};




class CDataGaussian:public CDataGen {
	int peaks;
	double z, sigma;
public:
	CDataGaussian(int numT, int numS, int numSR, double z0, int peaks0, double sigma0);
	void generateData ();
};

#endif

