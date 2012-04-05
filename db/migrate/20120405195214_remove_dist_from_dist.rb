class RemoveDistFromDist < ActiveRecord::Migration
  def up
    remove_column :dists, :dist
      end

  def down
    add_column :dists, :dist, :decimal
  end
end
