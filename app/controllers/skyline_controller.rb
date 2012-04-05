class SkylineController < ApplicationController
  
  def index
    flash[:notice] = "Star count = "+Star.count.to_s+", "
    flash[:notice] << "Food count = "+Food.count.to_s+", "
    flash[:notice] << "Dist count = "+Dist.count.to_s+", "
    flash[:notice] << "Price count = "+Price.count.to_s
  end

  def full_header
  	puts "\nSTARTING*****************************************************************\n"

    sorted_cols = []
    best_vptval = {}
    candidates_skyline = [] 
    InitUtil.load_sc_bv_cs( sorted_cols, best_vptval, candidates_skyline )
    
    puts "SORTED COLS "+sorted_cols.to_s
    puts "\nBEST VALUES FOR EACH VPT "+best_vptval.to_s
    puts "\nPRIME CANDIDATE SKYLINE LIST "+candidates_skyline.to_s

    header_point = {}
    InitUtil.upd_header_point( header_point, candidates_skyline )
    
    hp_updated = true    
    i = 1
    while(i<sorted_cols[0].count)
      puts i+1     
      hp_updated = false

      header_tuple = []
      sorted_cols.each do |vpt|
        puts vpt[i].to_s+"\n---"

      end
      i+=1
    end

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
