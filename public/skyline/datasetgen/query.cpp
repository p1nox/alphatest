#include "query.h"


CQuery::CQuery(int numAttr0) {
	numAttr=numAttr0;
	q=new double[numAttr];
	w=new double[numAttr];
	
	for (int i=0; i<numAttr; i++) q[i]=w[i]=0;
	k=0;
}


CQuery::~CQuery() {
	delete[] q;
	delete[] w;
}


double CQuery::getSumW() {
	double sumW = 0;
	for (int i=0; i<numAttr; i++) sumW+=w[i];
	return sumW;
}

std::ostream& operator << (std::ostream& o, CQuery* q) {
	char buffer[64];
	int i;

	o << "Q = [ ";
	for (i=0; i<q->numAttr; i++) {
		sprintf (buffer, "%2.4f", q->q[i]);
		o << buffer << " ";
	}

	o << "]\nW = [ ";
	
	for (i=0; i<q->numAttr; i++) {
		sprintf (buffer, "%2.4f", q->w[i]);
		o << buffer << " ";
	}

	o << "]\nK = " << q->k << "\n" << fflush;

	return o;
}


