class SkylineController < ApplicationController
  
  def index

  end

  def full_header
  	#puts "STARTING"
  	 	
  end  

  def null_header
  	
  end

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
