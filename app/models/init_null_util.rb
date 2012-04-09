class InitNullUtil < ActiveRecord::Base

  MAX = "DESC"
  MIN = "ASC"

  COLLECTION_NAME = ["stars","foods","dists","prices"] 
  FIELD_NAME = ["star","food","dist","price"]
  
  RULES = [MAX,MAX,MIN,MIN]
  RULES_COL = {"stars"=>MAX, "foods"=>MAX,"dists"=>MIN,"prices"=>MIN} #self.getRulesMap(RULES, COLLECTION_NAME)
  RULES_COLFIELD = {"star"=>MAX, "food"=>MAX,"dist"=>MIN,"price"=>MIN} #self.getRulesMap(RULES, FIELD_NAME)

  def self.load_sc_cs( sorted_col, candidates_skyline )
    vpt_star = Star.all(:order=>"value "+MAX)
    sorted_col <<  vpt_star
      vpt_food = Food.all(:order=>"value "+MAX)
      sorted_col << vpt_food
      vpt_dist = Dist.all(:order=>"value "+MIN)
      sorted_col << vpt_dist
      vpt_price = Price.all(:order=>"value "+MIN)
      sorted_col << vpt_price

      best_vptval = {}
      best_vptval["stars"] = vpt_star.first
      best_vptval["foods"] = vpt_food.first
      best_vptval["dists"] = vpt_dist.first
      best_vptval["prices"] = vpt_price.first     

      COLLECTION_NAME.each do |cn|
        bv = best_vptval[cn]
        tuple = {}
        tuple["id"] = bv.id
        tuple["star"] = Star.find_by_id(bv.id).value
        tuple["food"] = Food.find_by_id(bv.id).value
        tuple["dist"] = Dist.find_by_id(bv.id).value
        tuple["price"] = Price.find_by_id(bv.id).value
        upd_candidate_list( candidates_skyline, tuple )        
      end
      
  end


end