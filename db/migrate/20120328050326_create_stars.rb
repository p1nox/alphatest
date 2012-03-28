class CreateStars < ActiveRecord::Migration
  def change
    create_table :stars do |t|
      t.decimal :star

      t.timestamps
    end
  end
end
