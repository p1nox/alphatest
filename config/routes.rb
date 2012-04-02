Alphatest::Application.routes.draw do
  
  get "skyline/index"
  get "skyline/full_header"
  get "skyline/data_destroy_all"
  get "skyline/gen_uniform_data"
  get "skyline/gen_normal_data"
  

  root :to => 'dashboard#index'    

end
