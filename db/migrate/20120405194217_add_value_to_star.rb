class AddValueToStar < ActiveRecord::Migration
  def change
    add_column :stars, :value, :decimal

  end
end
