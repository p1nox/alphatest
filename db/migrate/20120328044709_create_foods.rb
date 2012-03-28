class CreateFoods < ActiveRecord::Migration
  def change
    create_table :foods do |t|
      t.decimal :food

      t.timestamps
    end
  end
end
