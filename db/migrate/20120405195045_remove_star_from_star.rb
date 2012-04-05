class RemoveStarFromStar < ActiveRecord::Migration
  def up
    remove_column :stars, :star
      end

  def down
    add_column :stars, :star, :decimal
  end
end
