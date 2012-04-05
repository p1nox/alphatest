class AddValueToPrice < ActiveRecord::Migration
  def change
    add_column :prices, :value, :decimal

  end
end
