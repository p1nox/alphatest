class RemovePriceFromPrice < ActiveRecord::Migration
  def up
    remove_column :prices, :price
      end

  def down
    add_column :prices, :price, :decimal
  end
end
