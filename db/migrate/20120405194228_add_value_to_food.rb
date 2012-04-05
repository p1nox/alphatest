class AddValueToFood < ActiveRecord::Migration
  def change
    add_column :foods, :value, :decimal

  end
end
