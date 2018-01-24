<?php

require_once(__DIR__ . '/CommonMacros.php');

class CustomerAdminTest extends CommonMacros
{
  public static function requests()
  {

    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       [[ 'POST', '/api/logout', [], null, null ]],

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [[ 'GET', '/api/address_book' ]],
       [[ 'GET', '/api/order' ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],
       [ 'send order back',
         [ 'PUT', '/api/order/${order-id}', null, function($map) {
             // Send the order back
             $order = $map['order'];
             $order['selectedQuote'] = null;
             $order['carrier'] = null;
             $order['service'] = null;

             return $order;
           }],
        [
          ],
        []],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}