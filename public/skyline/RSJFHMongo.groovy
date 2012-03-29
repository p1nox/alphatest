package util.common.skyline

@Grab(group='com.gmongo', module='gmongo', version='0.8')
import com.gmongo.GMongo

class RSJFHMongo {
	static MAX = -1
	static MIN = 1
	
	static COLLECTION_NAME = ["stars","foods","dists","prices"]
	static FIELD_NAME = ["star","food","dist","price"]
	static RULES = [MAX,MAX,MIN,MIN]
	static LIMIT = 100000
	
	static RULES_COL = getRulesMap(RULES, COLLECTION_NAME)
	static RULES_COLFIELD = getRulesMap(RULES, FIELD_NAME)
	
	static mongo = new GMongo()
	static db = mongo.getDB("randomnnull10")
	
	static CRONO = new Chronometer();
	
	static void main(def args){			
		println "STARTING"
		CRONO.start()
		
		//LISTA DE LOS MEJORES VALORES DE CADA VPT
		def best_vptval = [:] 		
		def sorted_col = [] //LISTA DE VPT ORDENADOS SEGUN QUERY
		
		//MEJORES VALORES DE CADA VPT 
		def iFIELD_NAME = FIELD_NAME.iterator()
		COLLECTION_NAME.each{ col_name ->
			def fname = iFIELD_NAME.next()			
			db[col_name] instanceof com.mongodb.DBCollection
			def sortedCol = db[col_name].find().sort([ ((String) fname) : RULES_COL[col_name] ]).limit(LIMIT)			
			sorted_col.add(sortedCol)				
			best_vptval.put(col_name, sortedCol.iterator().next())			
		}				
		
		//LISTA DE CANDIDATOS A SKYLINE (LLENANDO CON LOS PRIMEROS DE CADA VPT)
		def candidates_skyline = [] 
				
		//CANDIDATOS A SKYLINE (LLENANDO CON LOS PRIMEROS DE CADA VPT)
		best_vptval.each{ bval ->
			def tuple = [:]	
			def best_val = bval.getValue()
			tuple.put("_id",best_val["_id"])
						
			iFIELD_NAME = FIELD_NAME.iterator()
			COLLECTION_NAME.each{ col_name ->
				def fname = iFIELD_NAME.next()
				tuple.put( fname, db[col_name].findOne("_id":best_val["_id"])[fname] )
			}								
			updCandidateList(candidates_skyline, tuple)
		}		
		
		println "*PRIME CANDIDATE SKYLINE LIST: "+candidates_skyline
		
		//HEADER POINT (LISTA DE LOS PEORES VALORES DE CADA DIMENSION)
		def header_point = [:]		 		
		updHeaderPoint(header_point, candidates_skyline)		
		println "*PRIME HEADER POINT "+header_point		
		
		def hp_updated = true
		def i = 2		
		while( hp_updated && i<=sorted_col[0].size() ){
			println i+"----------------------------------------"
			hp_updated = false
			
			def header_tuple = []
			sorted_col.each{ vpt ->
				def vptRecord = vpt.next()
				//println "VPTRecord "+vptRecord				
				
				def updateable = true
				def vpt_candidate = [:] //JOIN DEL RECORD ACTUAL DEL VPT	
				vpt_candidate.put("_id",vptRecord["_id"])
				iFIELD_NAME = FIELD_NAME.iterator()
				COLLECTION_NAME.each{ col_name ->
					def fname = iFIELD_NAME.next()
					
					def vpt_value = vptRecord[fname]
					if(vpt_value==null){			
						vpt_value = db[col_name].findOne("_id":vptRecord["_id"])[fname]						
					}
					vpt_candidate.put( fname, vpt_value )
					
					if (RULES_COL[col_name]==MAX){
						if (vpt_value<header_point[fname]) {
							//println "Max "+col_name+":"+vpt_value+" hp:"+header_point[fname]
							updateable = false
						}
					}
					else { //MIN
						if (vpt_value>header_point[fname]) {
							//println "Min "+col_name+":"+vpt_value+" hp:"+header_point[fname]
							updateable = false
						}
					}
				}	
				//println vpt_candidate
				
				if( updateable ){
					header_tuple.add(vpt_candidate) 
					updCandidateList(candidates_skyline, vpt_candidate) //SE AGREGA A LA LISTA DE CANDIDATOS PORQUE LA TUPLA ACTUAL DOMINA EN TODAS LAS DIMENSIONES AL HP										
				}								
			}			
			println "HEADER TUPLES: "+header_tuple+"\nCANDIDATES UPDATED: "+candidates_skyline
			
			if(header_tuple.size()>0){
				updHeaderPoint(header_point, header_tuple)
				hp_updated = true				
			}
			
			i++;			
		}		
		
		CRONO.stop()
		
		showCandidateList(candidates_skyline)		
		CRONO.showTimeInfo()
	}
	
	static Map<String, Integer> getRulesMap(List<Integer> rule, List<String> name){
		def rule_col = [:]
		def irule = rule.iterator()
		name.each{ col ->			
			rule_col.put(col,irule.next())
		}
		return rule_col
	}		
	
	static void updCandidateList(List<Object> cand_sky, Map<Object, Object> vpt_cand){
		if(!cand_sky.contains(vpt_cand)){
			cand_sky.add(vpt_cand)
		}
	}
	
	static void updHeaderPoint(Map<Object, Object> hp, List<Object> ht){
		println "UPDATING HP "+hp+" "+ht
			
		FIELD_NAME.each{ fname ->	
			def cand_hp
			if(RULES_COLFIELD[fname]==MIN){ //si se esta minimizando en la dimension fname
				cand_hp = ht.sort({ a, b -> b[fname] <=> a[fname] } as Comparator).get(0)[fname]
				//println cand_hp+" "+hp[fname]
				if(hp[fname] < cand_hp || hp[fname]==null){
					hp.put(fname, cand_hp) //MAX first
				}				
			}
			else{ //si se esta maximizando en la dimension fname	
				cand_hp = ht.sort({ a, b -> a[fname] <=> b[fname] } as Comparator).get(0)[fname]
				//println cand_hp+" "+hp[fname]
				if(hp[fname] > cand_hp || hp[fname]==null){
					hp.put(fname, cand_hp) //MIN first
				}							
			}					
		}	
		
		println "NEW HEADER POINT "+hp
	}	
	
	static void showCandidateList(List<Object> lcandidate){
		println "\nCANDIDATES FINAL LIST***************************"
		lcandidate.eachWithIndex{ candidate,index ->
			println "Candidate "+index+" "+candidate
			
		}
	}
}
