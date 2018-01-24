<?php


/**
 * Assumes user sdaudlin with known password
 */
class TestSetup
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       [
        'Customer Admin Login',
        [ 'POST', '/api/login', [], null, ['username' => 'admin', 'password' => TestRunner::$admin_password ]]
        ],
       [[ 'POST', '/api/set-session-role/1' ]],
       [[ 'POST', '/api/system_property', null, [ 'name' => 'test_property', 'data' => 'test data' ]],
        ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}