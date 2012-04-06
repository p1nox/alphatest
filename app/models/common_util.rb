class CommonUtil < ActiveRecord::Base
	
	@now = nil

	def self.print_all( list )
		list.each do |l|
			puts l.id
		end
	end

	def self.time_start
		@now = Time.now
		puts "\nSTART_AT: "+@now.strftime("%I:%M %p")
	end

	def self.time_stop		
		puts "\nENDING TIME: "+(Time.now-@now).to_s
	end
	
end
