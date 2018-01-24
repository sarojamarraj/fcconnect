<?php


require_once(__DIR__ . '/CommonMacros.php');

class CarrierImageTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [[ 'GET', '/api/public/carrier/FedEx International Ground.png', [], null, null ], ],

       [[ 'GET', '/api/public/carrier/AAA Cooper Transportation.png', [], null, null ], ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       [ 'Logged in required',
         [ 'GET', '/api/public/carrier/AAA Cooper Transportation.png', [], null, null ],
         [],
         [],
         'Session expired',
         ],
       ];
  }
}