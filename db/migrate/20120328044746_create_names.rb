class CreateNames < ActiveRecord::Migration
  def change
    create_table :names do |t|
      t.decimal :name

      t.timestamps
    end
  end
end
