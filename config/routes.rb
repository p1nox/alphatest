Alphatest::Application.routes.draw do
  
  get "skyline/index"
  get "skyline/full_header"
  get "skyline/null_header"
  get "skyline/data_destroy_all"
  get "skyline/gen_uniform_data"
  get "skyline/gen_normal_data"
  get "skyline/gen_data_null"
  

  root :to => 'dashboard#index'    

end
