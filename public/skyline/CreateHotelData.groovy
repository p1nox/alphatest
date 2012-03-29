import java.util.List;
import java.util.Map;

package util.common.skyline

class CreateHotelData {
	static void main(def args){
		File hotelSrcFile = new File("/home/maverick/Escritorio/DATASK/base.data")
		File hotelNewFile = new File("/home/maverick/Escritorio/DATASK/uno.data")
		hotelNewFile.delete()
		hotelNewFile.createNewFile()
		
		def lineNumber = 0;
		100.times {
			hotelSrcFile.eachLine{ line ->
				def usrArg = line.split("	")
				hotelNewFile.append(++lineNumber+"	"+usrArg[1]+"$it\n")
			}
			println "Book "+lineNumber+" created"
		}
		
		println "Done!"
	}
}


/*def winHotels = []
hotels.each{ hotel ->
	if (winHotels.size()==0){
		winHotels.add(hotel)
	}
	else{
		winHotels.each{ whotel ->
			//whotel DOMINA hotel
			if( whotel[2]<=hotel[2] && whotel[3]<=hotel[3] && whotel[4]<=hotel[4] && (whotel[2]<hotel[2] || whotel[3]<hotel[3] || whotel[4]<hotel[4]) ){
				
			}
			else{ //hotel DOMINA whotel
				
			}
			if( ){
				
			}
		}
	}
}





package util.common.skyline

class RSJFH {
	
	static void main(def args){
		//def hotels = getHotels()
		
		def name = [:]
		name["1"] = "Aga"
		name["2"] = "Flo"
		name["3"] = "Kaz"
		name["4"] = "Neo"
		name["5"] = "Tor"
		name["6"] = "Uma"
		
		def stars = [:]
		stars["6"] = 2
		stars["3"] = 1
		stars["2"] = 1
		stars["5"] = 3
		stars["1"] = 3
		stars["4"] = 2
		
		def food = [:]
		food["3"] = 25
		food["2"] = 22
		food["1"] = 20
		food["6"] = 14
		food["5"] = 10
		food["4"] = 12
		
		def dist = [:]
		dist["3"] = 0.7
		dist["1"] = 1.2
		dist["2"] = 0.2
		dist["6"] = 0.2
		dist["4"] = 0.5
		dist["5"] = 0.5
		
		def price = [:]
		price["4"] = 1175
		price["2"] = 1237
		price["1"] = 750
		price["3"] = 2250
		price["6"] = 2550
		price["5"] = 980
		
		//def hotel = [name,stars,food,dist,price]		
		//SKYLINE OF STARS MAX, FOOD MAX, DIST MAX, PRICE MIN;
				
		def header_point = worstFirstTuple(stars,food,dist,price)
		def hp_updated = true
		
		int index = 1;
		while(hp_updated){
			
			def list_c = []
			for (index;index<stars.size();index++){			
				def stars_htuple = minTupleThan(header_point,completeTuple(stars[index].key(),stars,food,dist,price))
				def food_htuple = minTupleThan(header_point,completeTuple(food[index].key(),stars,food,dist,price))
				def dist_htuple = minTupleThan(header_point,completeTuple(dist[index].key(),stars,food,dist,price))
				def price_htuple = maxTupleThan(header_point,completeTuple(price[index].key(),stars,food,dist,price))
				list_c = [stars_htuple,food_htuple,dist_htuple,price_htuple]
			}	
			index=0
			hp_updated=false
			list_c.each{lc->
				if(lc==null){
					if(lc[0]>header_point[0]){
						header_point[0]=lc[0]
						hp_updated=true
					}
					if(lc[1]>header_point[1]){
						header_point[1]=lc[1]
						hp_updated=true
					}
					if(lc[2]>header_point[2]){
						header_point[2]=lc[2]
						hp_updated=true
					}
					if(lc[3]>header_point[3]){
						header_point[3]=lc[3]
						hp_updated=true
					}			
				}				
			}
			
		}
		
		
		
				
	}
	
	static Map<Object, Object> worstFirstTuple(Map<Object, Object> stars,Map<Object, Object> food,Map<Object, Object> dist,Map<Object, Object> price){
		//usar var indices
		def star_tuple = [stars[0],stars["3"],stars["3"],stars["4"]]
		def food_tuple = [food["6"],food[0],food["3"],food["4"]]
		def dist_tuple = [dist["6"],dist["3"],dist[0],dist["4"]]
		def price_tuple = [price["6"],price["3"],price["3"],price[0]]
		
		return [star_tuple.min(), food_tuple.min(), dist_tuple.min(), price_tuple.max()]
	}
	
	static Map<Object, Object> maxTupleThan(Map<Object, Object> header_point,Map<Object, Object> tuple){
		def htuple = null
		header_point.each{ hp->
			if( hp[0]<=tuple[0] && hp[1]<=tuple[1] && hp[2]<=tuple[2] && hp[3]<=tuple[3] && (hp[0]<tuple[0] || hp[1]<tuple[1] || hp[2]<tuple[2] || hp[3]<tuple[3]) ){
				return tuple
			}
		}	
		return htuple
	}
	
	static Map<Object, Object> minTupleThan(Map<Object, Object> header_point,Map<Object, Object> tuple){
		def htuple = null
		header_point.each{ hp->
			if( hp[0]>=tuple[0] && hp[1]>=tuple[1] && hp[2]>=tuple[2] && hp[3]>=tuple[3] && (hp[0]>tuple[0] || hp[1]>tuple[1] || hp[2]>tuple[2] || hp[3]>tuple[3]) ){
				return tuple
			}
		}
		return htuple
	}
	
	static Map<Object, Object> completeTuple(level,stars,food,dist,price){
		return [stars[level],food[level],dist[level],price[level]]
	}
	
	static List getHotels(){
		File hotelSrcFile = new File("/home/maverick/Escritorio/DATASK/base.data")
		def lineNumber = 0;
		def lhotels = [];
		hotelSrcFile.eachLine{ line ->
				def usrArg = line.split(" ")
				//dist stars price
				def lhotel = [usrArg[0],usrArg[1],usrArg[2],usrArg[3],usrArg[4]]
				lhotels.add(lhotel)
		}
		
		/*lhotels.each{ hotel ->
			println hotel[0]
		}*/
		
		return lhotels;
	}
	
}

*
*
*
*
*
*
*
*
*
*
*
*
*
*/




