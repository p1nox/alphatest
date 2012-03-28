class DashboardController < ApplicationController
  
  def index
  	# a = Star.new
  	# a.star = 12
  	# a.save

  	@b = Star.find_by_id(1)
  	
  end
  
end
