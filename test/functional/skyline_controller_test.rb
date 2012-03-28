require 'test_helper'

class SkylineControllerTest < ActionController::TestCase
  test "should get index" do
    get :index
    assert_response :success
  end

  test "should get full_header" do
    get :full_header
    assert_response :success
  end

end
