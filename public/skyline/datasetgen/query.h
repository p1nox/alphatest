#ifndef __QUERY_H
#define __QUERY_H

#include "misc.h"


class CQuery {
public:
	int numAttr; //number of attributes in the query
	double* q;   // targer values for attributes
	double* w;   // attributes weights in query
	int k;       // number of top-k qnswer requested

	CQuery(int numAttr0);
	~CQuery();

	double getSumW();
	friend std::ostream& operator << (std::ostream& o, CQuery* q);
};


#endif

