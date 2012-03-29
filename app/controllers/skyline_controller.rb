class SkylineController < ApplicationController
  
  def index

  end

  def full_header
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
			def sortedCol = db[col_name].find().sort([ ((String) fname) : RULES_COL[col_name] ])//.limit(LIMIT)			
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
				println "TUPLE "+vptRecord["_id"]
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
  end  

  def null_header
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
  end

end
