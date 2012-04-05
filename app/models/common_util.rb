class CommonUtil < ActiveRecord::Base
	
	def self.print_all( list )
		list.each do |l|
			puts l.id
		end
	end

	
end
