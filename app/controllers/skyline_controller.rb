class SkylineController < ApplicationController
  
  def index
    flash[:notice] = "Star count = "+Star.count.to_s+", "
    flash[:notice] << "Food count = "+Food.count.to_s+", "
    flash[:notice] << "Dist count = "+Dist.count.to_s+", "
    flash[:notice] << "Price count = "+Price.count.to_s
  end

  def full_header
  	puts "\nSTARTING*****************************************************************\n"
    CommonUtil.time_start

    sorted_cols = []    
    candidates_skyline = [] 
    InitUtil.load_sc_cs( sorted_cols, candidates_skyline )
    
    puts "SORTED COLS "+sorted_cols.to_s    
    puts "\nPRIME CANDIDATE SKYLINE LIST "+candidates_skyline.to_s

    header_point = {}
    InitUtil.upd_header_point( header_point, candidates_skyline )
    
    hp_updated = true    
    i = 1
    while(hp_updated || i<sorted_cols[0].count)
      puts i+1     
      hp_updated = false

      header_tuple = []
      sorted_cols.each do |vpt|
        puts vpt[i].id.to_s+"\n---"

        vpt_candidate = {}  
        InitUtil.load_complete_tuple( vpt[i].id, vpt_candidate )

        updateable = true
        InitUtil::FIELD_NAME.each do |fn|                    
          if InitUtil::RULES_COLFIELD[fn]==InitUtil::MAX
            if vpt_candidate[fn]<header_point[fn]
              puts fn+" MAX "+vpt_candidate[fn].to_s+"  "+header_point[fn].to_s
              updateable = false
              break
            end
          else
            if vpt_candidate[fn]>header_point[fn]
              puts fn+" MIN "+vpt_candidate[fn].to_s+"  "+header_point[fn].to_s
              updateable = false
              break
            end
          end          
        end  
        
        if updateable
          header_tuple << vpt_candidate    
          InitUtil.upd_candidate_list( candidates_skyline, vpt_candidate )        
        end 

      end
      puts "\nHEADER TUPLES AND CANDIDATES UPD "+header_tuple.to_s+" "+candidates_skyline.count.to_s
      
      if header_tuple.count>0
        InitUtil.upd_header_point( header_point, header_tuple )
        hp_updated = true
      end

      i+=1
    end            
    
    CommonUtil.time_stop
    InitUtil.show_candidate_list( candidates_skyline )
  end  

  def null_header
  	
  end



  # DATA GENERATION ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
  def gen_uniform_data  	
    ntuples = params[:ntuple]

    if !ntuples.blank?
    	VptUtil.gen_data_uniform( ntuples.to_i )
    	flash[:success] = ntuples+" tuples generated with uniform distribution"
    else
      flash[:error] = "No tuple number given"
    end

    redirect_to skyline_index_path
  end

  def gen_normal_data
     ntuples = params[:ntuple]

    if !ntuples.blank?
    	VptUtil.gen_data_normal( ntuples.to_i )
    	flash[:success] = ntuples+" tuples generated with normal distribution"
    else
      flash[:error] = "No tuple number given"
    end

  	redirect_to skyline_index_path
  end

  def data_destroy_all
  	VptUtil.destroy_all
  	flash[:success] = "Data droped"
  	redirect_to skyline_index_path
  end

end
