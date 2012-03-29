package util.common.skyline

import java.util.Iterator;

@Grab(group='com.gmongo', module='gmongo', version='0.8')
import com.gmongo.GMongo

class BucketNullMongo {
	static MAX = -1
	static MIN = 1
	static NULL_FIRST = 1
	static NULL_BOTTOM = -1
	
	static COLLECTION_NAME = ["stars","foods","dists","prices"]
	static FIELD_NAME = ["star","food","dist","price"]
	static RULES = [MAX,MAX,MIN,MIN]
	static LIMIT = 100000
	
	static RULES_COL = getRulesMap(RULES, COLLECTION_NAME)
	static RULES_COLFIELD = getRulesMap(RULES, FIELD_NAME)
	
	static mongo = new GMongo()
	static db = mongo.getDB("randomnull10")
	
	static CRONO = new Chronometer();
	
	static void main(def args){			
		println "STARTING"
		CRONO.start()
		
		//NULL Bucket INIT AND SORTING DATA:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		def null_bucket = [] //LISTA DE ID QUE REFERENCIAN A UN VALOR NULL EN SU RESPECTIVA DIMENSION
		def dim_edges = [:] //BORDE SUPERIOR E INFERIOR DE VALORES NO NULOS DE SU RESPECTIVA DIMENSION
		def best_vptval = [:]
		def sorted_col = [] //LISTA DE VPT ORDENADOS SEGUN QUERY
		
		def iFIELD_NAME = FIELD_NAME.iterator()
		COLLECTION_NAME.each{ col_name ->
			def fname = iFIELD_NAME.next()
			db[col_name] instanceof com.mongodb.DBCollection
			//db[col_name].ensureIndex(["_id":1],[unique:true])
			def sortedCol = db[col_name].find().sort([ ((String) fname) : NULL_FIRST ]).toArray() //.limit(LIMIT)
			//println sortedCol			
			sorted_col.add(sortedCol)
			
			def i = 0
			//def nulls = []
			def last_rec
			for (Iterator isortcol = sortedCol.iterator(); isortcol.hasNext();) {
				last_rec = isortcol.next()
				//println i+" u "+last_rec
				if(last_rec[fname]==null){
					null_bucket.add(((Integer) last_rec["_id"]))
					i++
				}
				else break //necesario hacer el for para terminar el ciclo al leer todos los nulos
			}											
			
			//null_bucket.put(fname,nulls)			
			
			def dim_size = sortedCol.size()-1					
			if(RULES_COLFIELD[fname]==MIN){
				//println i+" "+last_rec
				best_vptval.put(fname, last_rec)
				dim_edges.put(fname, [i+1,dim_size])				
			}
			else{
				//println dim_size+" "+sortedCol.reverse().iterator().getAt(0)
				best_vptval.put(fname, sortedCol.reverse().iterator().next())
				dim_edges.put(fname, [dim_size-1,i])
			}	
		}
		
		println "Null bucket dimension id "+null_bucket
		println "Edges by dimension "+dim_edges
		println "Best values by dimension "+best_vptval
		
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
			println "del "+tuple["_id"]+" from "+null_bucket
			null_bucket = null_bucket.minus(tuple["_id"])
			println null_bucket
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
			def ifname = FIELD_NAME.iterator()
			sorted_col.each{ vpt ->		
				def f_name = ifname.next()
				def vptRecord = vpt.iterator().getAt(dim_edges[f_name][0])
				dim_edges[f_name][0] = dim_edges[f_name][0]+RULES_COLFIELD[f_name] //siguiente indice segun la dimension y las reglas que requiera
				//println "VPTRecord "+vptRecord+" "+dim_edges[f_name][0]
				
				def unknown_value = []
				def updateable = true
				def vpt_candidate = [:] //JOIN DEL RECORD ACTUAL DEL VPT
				vpt_candidate.put("_id",vptRecord["_id"])
				println "TUPLE "+vptRecord["_id"]
				iFIELD_NAME = FIELD_NAME.iterator()
				COLLECTION_NAME.each{ col_name ->
					def fname = iFIELD_NAME.next()
					
					def vpt_value = db[col_name].findOne("_id":vptRecord["_id"])[fname]
					
					vpt_candidate.put( fname, vpt_value )
					
					if(vpt_value!=null){ //los demas casos se toman en cuenta al actualizar el HP
						unknown_value.add(false)
						if(header_point[fname]!=null){
							if (RULES_COL[col_name]==MAX){
								if (vpt_value<header_point[fname]) {
									println "Max "+col_name+":"+vpt_value+" hp:"+header_point[fname]
									updateable = false									
								}
							}
							else { //MIN
								if (vpt_value>header_point[fname]) {
									println "Min "+col_name+":"+vpt_value+" hp:"+header_point[fname]
									updateable = false									
								}
							}
						}// val!=null && hp==null => true
					}
					else{
						unknown_value.add(true)
					}	
					//else updateable = false si el valor es null no se puede descartar, asi que se asume true porque tienen que ser probadas las demas dimensiones							
					
				}
				//println vpt_candidate
				
				if( updateable && !completeNull(unknown_value) ){
					header_tuple.add(vpt_candidate)
					
					updCandidateList(candidates_skyline, vpt_candidate) //SE AGREGA A LA LISTA DE CANDIDATOS PORQUE LA TUPLA ACTUAL DOMINA EN TODAS LAS DIMENSIONES AL HP
				}				
				println "del "+vpt_candidate["_id"]+" from "+null_bucket
				null_bucket = null_bucket.minus(vpt_candidate["_id"])
				println null_bucket
			}
			println "HEADER TUPLES: "+header_tuple+"\nCANDIDATES UPDATED: "+candidates_skyline
			
			if(header_tuple.size()>0){
				updHeaderPoint(header_point, header_tuple)
				hp_updated = true
			}
			
			i++;
		}
		
		candidates_skyline = mergeCandidatesWithNulls(candidates_skyline, null_bucket, header_point)
		
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
		println "updCanWith "+vpt_cand
		if(!cand_sky.contains(vpt_cand)){
			cand_sky.add(vpt_cand)			
		}
	}
	
	static void updHeaderPoint(Map<Object, Object> hp, List<Object> ht){
		println "UPDATING HP "+hp+" "+ht
			
		FIELD_NAME.each{ fname ->	
			def cand_hp = null, can_list
			if(RULES_COLFIELD[fname]==MIN){ //si se esta minimizando en la dimension fname
				can_list = ht.sort({ a, b -> b[fname] <=> a[fname] } as Comparator)				
				can_list.each{ c ->
					if(cand_hp==null && c[fname]!=null){
						cand_hp = c[fname]
						//break
					}
				}				
				println cand_hp+" "+hp[fname]				
				if(hp[fname] > cand_hp || hp[fname]==null){
					hp.put(fname, cand_hp) //MIN first
				}		
			}
			else{ //si se esta maximizando en la dimension fname	
				can_list = ht.sort({ a, b -> a[fname] <=> b[fname] } as Comparator)				
				can_list.each{ c ->
					if(cand_hp==null && c[fname]!=null){
						cand_hp = c[fname]
						//break
					}
				}				
				println cand_hp+" "+hp[fname]				
				if(hp[fname] < cand_hp || hp[fname]==null){
					hp.put(fname, cand_hp) //MAX first
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
	
	static List<Object> mergeCandidatesWithNulls(List<Object> c_lis, List<Object> null_bucket, Map<Object, Object> header_point){ //con el hp o con candi_lis? seria logico por el header point debido a que todos los demas puntos se podaron de esas forma
		println "Testing The NULL BUCKET "+null_bucket
		
		def bucket_val_to_add = []
		null_bucket.each{ nb_id ->
			println "NULL "+nb_id+"------"
			def null_tuple = [:]
			
			def unknown_value = []
			def addable = true
			def iFIELD_NAME = FIELD_NAME.iterator()
			COLLECTION_NAME.each{ col_name ->
				def fname = iFIELD_NAME.next()
				db[col_name] instanceof com.mongodb.DBCollection
				def bucket_value = db[col_name].findOne("_id":nb_id)[fname]
				null_tuple.put( fname, bucket_value)
				
				if(bucket_value==null){
					unknown_value.add(true)
				}
				else{
					unknown_value.add(false)
					if(header_point[fname]!=null){
						if (RULES_COL[col_name]==MAX){
							if (bucket_value<header_point[fname]) {
								println "MaxNULL "+col_name+":"+bucket_value+" hp:"+header_point[fname]
								addable = false
							}
						}
						else { //MIN
							if (bucket_value>header_point[fname]) {
								println "MinNULL "+col_name+":"+bucket_value+" hp:"+header_point[fname]
								addable = false
							}
						}
					}					
				}				
			}	
			
			if(addable && !completeNull(unknown_value))	{
				bucket_val_to_add.add(null_tuple)
			}
		}		
		
		return c_lis+bucket_val_to_add
	}
	
	static boolean completeNull(List<Object> unknown_value){
		//println unknown_value.count(true)==unknown_value.size()
		return unknown_value.count(true)==unknown_value.size()
	}
	
	
}

