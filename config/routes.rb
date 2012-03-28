Alphatest::Application.routes.draw do
  
  get "skyline/index"
  get "skyline/full_header"

  root :to => 'dashboard#index'    

end
