package util.common.skyline

@Grab(group='com.gmongo', module='gmongo', version='0.8')
import com.gmongo.GMongo

class GenDataFH {
	static DB_TO_COPY = "uniformefh1kk"
	static CRONO = new Chronometer();
	static mongo = new GMongo()
	static db = mongo.getDB(DB_TO_COPY)
	
	static COLLECTION_NAME = ["stars","foods","dists","prices"]
	static FIELD_NAME = ["star","food","dist","price"]
	
	static void main(def args){
		println "START"
		
		File data_source = new File("/arca/USB/PreferenciasBD/DATA/DataUniforme/"+DB_TO_COPY+".data")
		
		def iCOLLECTION_NAME = COLLECTION_NAME.iterator()
		def iFIELD_NAME = FIELD_NAME.iterator()
		data_source.eachLine{ line ->
			if(line && line[0]=="["){				
				def fname = iFIELD_NAME.next()
				def cname = iCOLLECTION_NAME.next()
				db[cname] instanceof com.mongodb.DBCollection
				
				def record = line.replace("[ ","").replace(" ]","").split(" ")				
				record.eachWithIndex{ r,i ->
					db[cname].insert("_id":i+1, ((String) fname):r)
					//println r
				}
				
			}			
		}
		
		println "DONE"
	}		
	
}
