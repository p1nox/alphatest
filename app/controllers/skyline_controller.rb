class SkylineController < ApplicationController
  
  def index

  end

  def full_header
  	#puts "STARTING"
  	 	
  end  

  def null_header
  	
  end

  def gen_uniform_data  	
  	VptUtil.gen_data_uniform( 100 )
  	flash[:success] = "Data generated with uniform distribution!"
  	redirect_to skyline_index_path
  end

  def gen_normal_data
  	VptUtil.gen_data_normal( 100 )
  	flash[:success] = "Data generated with normal distribution!"
  	redirect_to skyline_index_path
  end

  def data_destroy_all
  	VptUtil.destroy_all
  	flash[:success] = "Data droped!"
  	redirect_to skyline_index_path
  end

end
