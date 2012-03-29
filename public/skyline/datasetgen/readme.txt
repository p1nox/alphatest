#################################################################
#								#
#								#
#	Synthetic Data generation Code				#
#	11/22/02						#
#	Contact amelie@cs.columbia.edu for more information	#
#								#
#								#
#################################################################

TO COMPILE:
----------------
g++ *.cpp -o output
./output 
	
misc.h - misc.cpp
-----------------

	Contains varous functions:
		* set Random seed
		* Random number generator
		* Quicksort
		* Gaussian data generator
		
		
query.h - query.cpp
-------------------

	Data structure for queries
	
datagen.h - datagen.cpp
-----------------------	

	Generated Queries and Databases:
		* Querygen: generates numQ queries, target values and weight are generated using uniform distribution
		* Datagen : generates a database with numT objects and numS attributes, source access costs. Data values are stored in **d
			- Uniform Data Sets (as described in ICDE'02 paper): generated using the CDataGenUniform class
			- Gaussian Data Sets (as described in ICDE'02 paper): generated using the CDataGaussian class with peaks0 picks, sigma of sigma0 and zipfian parameter z0 (multidimensional Gaussian Bells)
			- Correlated Data Sets (as described in ICDE'02 paper): generated using the CDataGenCorrelated class with sigma0 as the coreelation factor cf.
main.cpp
--------

	Print results from example calls to the functions.
	Do not forget to set the seed before generating the data.