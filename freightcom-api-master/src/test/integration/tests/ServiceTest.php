<?php

require_once(__DIR__ . '/CommonMacros.php');

class ServiceTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::customer_login_request(),
       self::login_lookup_request('CUSTOMER_ADMIN'),
       self::$login_set_role_request,


       // Tests start

       [ 'get services',
         [ 'GET', '/api/service' ],
         ],

       // Logout

       ['logout', [ 'POST', '/api/logout', [], null, null ]],

       ];
  }
}