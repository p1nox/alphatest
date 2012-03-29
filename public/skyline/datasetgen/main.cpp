#include "misc.h"
#include "query.h"
#include "datagen.h"
#include <iostream>

using namespace std;

int main() {

	SetSeed(1234);
	// Uniform CDataGenUniform(a,b,c)      CDataGenUniform(int numT, int numS, int numSR):CDataGen(numT, numS, numSR)
	//   a: numero de filas
	//   b: numero de columnas
	//   c: ?...=1
	//   ie => CDataGenUniform(10, 3, 1)
	// Gaussian CDataGaussian(a,b,c,z,N,distinctValues)      CDataGaussian(int numT, int numS, int numSR, double z0, int peaks0, double sigma0)
	//   a: numero de filas
	//   b: numero de columnas
	//   c: ?...=1
	//   z: zipfian parameter
	//         la palabra mas frecuente ocurrira aproximadamente 2 veces mas que la segunda palabra mas frecuente...
	//   N: total number of tuples
	//   distinctValues:total number of distinct tuples
	//   ie => CDataGaussian(10, 3, 1, 0, 10, 10)
	// Correlated CDataGenCorrelated(a,b,c,distinctValues)      CDataGenCorrelated(int numT, int numS, int numSR, double sigma0)
	//   a: numero de filas
	//   b: numero de columnas
	//   c: ?...=1
	//   distinctValues:total number of distinct tuples??? or without negative numbers with value 1
	//   ie => CDataGenCorrelated(10, 3, 1, 1)
	CDataGen *data = new CDataGenUniform(10, 4, 1);//new CDataGenCorrelated(10, 4, 1, 1); //new CDataGaussian(10, 4, 1, 0, 10, 10); //new CDataGenUniform(10, 4, 1);
	CQueryGen *query = new CQueryGen( 1, 2, 1, 10, 2);
	data->generate();

	//CONSOLE
	//cout<<"DATA\n"<<data<<endl;
	//cout<<"QUERY\n"<<query->q[0]<<endl;

	//FILE
	ofstream fdata("dataset.data");		
		fdata<<"DATA\n"<<data<<endl;
		fdata<<"QUERY\n"<<query->q[0]<<endl;
	fdata.close();

	return 0;

}

// TO COMPILE:
// ----------------
// g++ *.cpp -o output
// ./output 
