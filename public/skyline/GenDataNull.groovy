package util.common.skyline

@Grab(group='com.gmongo', module='gmongo', version='0.8')
import com.gmongo.GMongo

class GenDataNull {
	static CRONO = new Chronometer();
	static COLLECTION_NAME = ["stars","foods","dists","prices"]
	static FIELD_NAME = ["star","food","dist","price"]
	
	static void main(def args){
		println "STARTING"		
		def mongo = new GMongo()
		def db = mongo.getDB("randomnull10")
		
		int total_hotels = 40
		int total_null = 4
		
		db["names"] instanceof com.mongodb.DBCollection
		db["stars"] instanceof com.mongodb.DBCollection
		db["foods"] instanceof com.mongodb.DBCollection
		db["dists"] instanceof com.mongodb.DBCollection
		db["prices"] instanceof com.mongodb.DBCollection
		
		CRONO.start()
		1.upto(total_hotels){
			db["names"].save("_id":it, "name":"hotel$it")
			db["stars"].save("_id":it, "star":getRStar(it))
			db["foods"].save("_id":it, "food":getRFood(it))
			db["dists"].save("_id":it, "dist":getRDist(it))
			db["prices"].save("_id":it, "price":getRPrice(it))
			println ".$it"
		}		
		
		int i = 1
		while(i<=total_null){
			def iFIELD_NAME = FIELD_NAME.iterator()
			COLLECTION_NAME.each{cname ->
				def fname = iFIELD_NAME.next()
				db[cname].save("_id":i, ((String) fname):null)
				i++
			}
		}
		
		println "DONE!"
		CRONO.stop()
		CRONO.showTimeInfo()
	}	
	
	static Integer getRStar(int seed){		
		
		return new Random(seed).nextInt(9) + 1
	}
	
	static Integer getRFood(int seed){
		
		return new Random(seed).nextInt(31)
	}
	
	static Integer getRDist(int seed){
		
		return new Random(seed).nextInt(99) + 1
	}	
	
	static Integer getRPrice(int seed){
		
		return new Random(seed).nextInt(999) + 1
	}
	
	
	
}
