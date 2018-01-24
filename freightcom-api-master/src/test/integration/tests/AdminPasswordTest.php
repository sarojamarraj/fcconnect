<?php

require_once(__DIR__ . '/CommonMacros.php');

class AdminPasswordTest extends CommonMacros
{
  public static function requests()
  {
    return
      [
       self::customer_login_request('admin'),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'Change admin password',
         [ 'PUT', '/api/user/update/${user-id}', null, [ 'password' => 'aaaa' ]],
         ],

       [ 'Change admin password back',
         [ 'PUT', '/api/user/${user-id}', null, [ 'password' => getenv('FREIGHTCOM_admin')  ]],
         ],

       [ 'Logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}
