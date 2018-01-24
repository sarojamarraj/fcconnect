<?php

require_once(__DIR__ . '/CommonMacros.php');

class ShipQueryTest extends CommonMacros
{
  public static function requests()
  {

    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'get a range of ship dates',
         [ 'GET', '/api/submitted-orders',
           [ 'scheduledShipDate' => 'custom',
             'scheduledShipDateFrom' => '2017-02-01',
             'scheduledShipDateTo' => '2017-02-02',
             ]
           ],
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],


       ];
  }
}
