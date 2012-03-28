class CreateDists < ActiveRecord::Migration
  def change
    create_table :dists do |t|
      t.decimal :dist

      t.timestamps
    end
  end
end
