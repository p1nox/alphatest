class AddValueToDist < ActiveRecord::Migration
  def change
    add_column :dists, :value, :decimal

  end
end
