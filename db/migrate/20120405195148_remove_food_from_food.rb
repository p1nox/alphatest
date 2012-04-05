class RemoveFoodFromFood < ActiveRecord::Migration
  def up
    remove_column :foods, :food
      end

  def down
    add_column :foods, :food, :decimal
  end
end
