class VptUtil < ActiveRecord::Base

  def self.gen_data_uniform( num_tuple ) #uniform(BOTTOM,TOP)
    r = SimpleRandom.new
    (1..num_tuple).each do |i|      
      #puts r.uniform(1,5).to_s+" "+r.uniform.to_s+" "+r.uniform.to_s+" "+r.uniform.to_s
      create_tuple(i, r.uniform,r.uniform,r.uniform,r.uniform)
      puts "Tuple #{i} generated"
    end    
  end

  def self.gen_data_normal(  num_tuple ) #normal(MEDIA,DESVIACIONESTANDAR)
    r = SimpleRandom.new
    (1..num_tuple).each do |i|      
      #puts r.normal(5,1).to_s+" "+r.normal(5,1).to_s+" "+r.normal(5,1).to_s+" "+r.normal(5,1).to_s
      create_tuple(i, r.normal(5, 1),r.normal(5, 1),r.normal(5, 1),r.normal(5, 1))
      puts "Tuple #{i} generated"
    end    
  end

  def self.create_tuple( id, star, food, dist, price )
    s = Star.new
    s.star = star
    s.id = id
    s.save
    f = Food.new
    f.food = food
    f.id = id
    f.save
    d = Dist.new
    d.dist = dist
    d.id = id
    d.save
    p = Price.new
    p.price = price  
    p.id = id
    p.save
    # n = Name.new
    # n.name = name
    # n.save
  end

  def self.destroy_all
    Star.destroy_all
    Food.destroy_all
    Dist.destroy_all
    Price.destroy_all
    #Name.destroy_all
  end


end