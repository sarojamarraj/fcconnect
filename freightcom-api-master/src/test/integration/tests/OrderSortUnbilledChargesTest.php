<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderSortUnbilledChargesTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'Sort by unbilled charges',
         [ 'GET', '/api/submitted-orders', [ 'sort' => 'unbilledCharges,desc' ]],
         [ 'charges' => '_embedded.customerOrders.*.unbilledCharges' ],
         []
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}