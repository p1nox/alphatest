class VptUtil < ActiveRecord::Base

  def self.gen_data_uniform( num_tuple ) #uniform(BOTTOM,TOP)
    r = SimpleRandom.new
    (1..num_tuple).each do |i|      
      #puts r.uniform(1,5).to_s+" "+r.uniform.to_s+" "+r.uniform.to_s+" "+r.uniform.to_s
      create_tuple(i, r.uniform,r.uniform,r.uniform,r.uniform)
      puts "Tuple #{i} generated (uniform)"
    end    
  end

  def self.gen_data_normal(  num_tuple ) #normal(MEDIA,DESVIACIONESTANDAR)
    r = SimpleRandom.new
    (1..num_tuple).each do |i|      
      #puts r.normal(5,1).to_s+" "+r.normal(5,1).to_s+" "+r.normal(5,1).to_s+" "+r.normal(5,1).to_s
      create_tuple(i, r.normal(5, 1),r.normal(5, 1),r.normal(5, 1),r.normal(5, 1))
      puts "Tuple #{i} generated (normal)"
    end    
  end

  def self.gen_data_null( num_tuple, null_percent, distribution ) 
    if distribution=="normal"
      gen_data_normal(num_tuple)
    else
      gen_data_uniform(num_tuple)
    end

    null_count = (10*num_tuple)/100
    puts null_count
    icn = 1
    (1..10).each do |i|
      cn = InitUtil::COLLECTION_NAME[icn]
      vpt_rec = nil

      if cn=="star"     
        vpt_rec = Star.find_by_id(i) 
      elsif cn=="food"
        vpt_rec = Food.find_by_id(i) 
      elsif cn=="dist"
        vpt_rec = Dist.find_by_id(i) 
      else 
        #price
        vpt_rec = Price.find_by_id(i) 
      end        
      vpt_rec.value = nil
      vpt_rec.update
            
      icn = ((icn<4) ? icn+1 : 1)      
    end
  end  

  def self.create_tuple( id, star, food, dist, price )
    s = Star.new
    s.value = star
    s.id = id
    s.save
    f = Food.new
    f.value = food
    f.id = id
    f.save
    d = Dist.new
    d.value = dist
    d.id = id
    d.save
    p = Price.new
    p.value = price  
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