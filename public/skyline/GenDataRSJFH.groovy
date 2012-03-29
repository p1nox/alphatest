package util.common.skyline

@Grab(group='com.gmongo', module='gmongo', version='0.8')
import com.gmongo.GMongo

class GenDataRSJFH {
	static CRONO = new Chronometer();
	
	static void main(def args){
		println "STARTING"		
		def mongo = new GMongo()
		def db = mongo.getDB("randomnnull10")
		
		int total_hotels = 40
		
		db.names instanceof com.mongodb.DBCollection
		db.stars instanceof com.mongodb.DBCollection
		db.foods instanceof com.mongodb.DBCollection
		db.dists instanceof com.mongodb.DBCollection
		db.prices instanceof com.mongodb.DBCollection
		
		CRONO.start()
		1.upto(total_hotels) {
			db.names.insert("_id":it, "name":"hotel$it")
			db.stars.insert("_id":it, "star":getRStar(it))
			db.foods.insert("_id":it, "food":getRFood(it))
			db.dists.insert("_id":it, "dist":getRDist(it))
			db.prices.insert("_id":it, "price":getRPrice(it))
			println ".$it"
		}		
		
		println "DONE!"
		CRONO.stop()
		CRONO.showTimeInfo()
	}	
	
	static int getRStar(int seed){
		return new Random(seed).nextInt(9) + 1
	}
	
	static int getRFood(int seed){
		return new Random(seed).nextInt(31)
	}
	
	static int getRDist(int seed){
		return new Random(seed).nextInt(99) + 1
	}	
	
	static int getRPrice(int seed){
		return new Random(seed).nextInt(999) + 1
	}
	
}
