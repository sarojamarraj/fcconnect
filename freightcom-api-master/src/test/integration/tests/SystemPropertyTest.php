<?php

require_once(__DIR__ . '/CommonMacros.php');


/**
 * Assumes user sdaudlin with known password
 */
class SystemPropertyTest extends CommonMacros
{
  public static function requests()
  {
    $rand_test_data = 'test data ' . substr(md5(mt_rand()), 0, 8);
    $rand_test_data_2 = 'test data_2 ' . substr(md5(mt_rand()), 0, 8);
    $rand_test_del_data = 'test data del ' . substr(md5(mt_rand()), 0, 8);

    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [[ 'GET', '/api/system_property' ],
        [
         'count' => '_embedded.systemProperties.*',
         ],
        [
         'count' => '>0',
         ],],
       [[ 'GET', '/api/system_property/welcome_title' ]],
       [[ 'GET', '/api/system_property/welcome_message' ]],
       [[ 'PUT', '/api/system_property/test_property', null, [ 'data' => 'test data' ]]],
       [[ 'GET', '/api/system_property/test_property' ],
        [ 'data' => 'data', ],
        [ 'data' => 'test data' ],
        ],
       [[ 'PUT', '/api/system_property/test_property', null, [ 'data' => $rand_test_data ]]],
       [[ 'GET', '/api/system_property/test_property' ],
        [ 'data' => 'data', 'test-id' => 'id' ],
        [ 'data' => $rand_test_data, 'test-id' => '@notnull', ],
        ],
       // Test updating by id
       [[ 'PUT', '/api/system_property/${test-id}', null, [ 'data' => $rand_test_data_2 ]]],
       [[ 'GET', '/api/system_property/${test-id}' ],
        [ 'data' => 'data', ],
        [ 'data' => $rand_test_data_2 ],
        ],
       [[ 'POST', '/api/system_property', null, [
                                                 'name' => 'test_property_del',
                                                 'data' => $rand_test_del_data ]],
        [ 'delete-id' => 'id', 'data' => 'data' ],
        [ 'delete-id' => '@notnull', 'data' => $rand_test_del_data ],
        ],
       [[ 'GET', '/api/system_property/test_property_del' ],
        [],
        [],
        ],
       [ 'check first delete', [ 'DELETE', '/api/system_property/${delete-id}' ], ],
       [ 'check second delete', [ 'DELETE', '/api/system_property/${delete-id}' ],
         [],
         [],
         'No such systemProperty',
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       // Try accessing as customer admin

       self::customer_login_request(),
       self::login_lookup_request('CUSTOMER_ADMIN'),
       self::$login_set_role_request,

       [[ 'GET', '/api/system_property' ],
        [
         'count' => '_embedded.systemProperties.*',
         ],
        [
         'count' => '>0',
         ],],
       [[ 'GET', '/api/system_property/welcome_title' ]],
       [[ 'GET', '/api/system_property/welcome_message' ]],

       [[ 'PUT', '/api/system_property/test_property', null, [ 'data' => $rand_test_data ]],
        [],
        [],
        'Access is denied'
        ],
       [[ 'PUT', '/api/system_property/${test-id}', null, [ 'data' => $rand_test_data_2 ]],
        [],
        [],
        'Access is denied'
        ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       ];
  }
}