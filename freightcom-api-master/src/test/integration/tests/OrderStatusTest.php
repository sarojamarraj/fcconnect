<?php

require_once(__DIR__ . '/CommonMacros.php');

class OrderStatusTest extends CommonMacros
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

       [ 'get order statuses',
         [ 'GET', '/api/orderStatuses' ],
         [ 'count' => '_embedded.orderStatuses.@size',
           'sequence' => '_embedded.orderStatuses.*.sequence',
           'sequence1' => '_embedded.orderStatuses.1.sequence',
           'sequence3' => '_embedded.orderStatuses.3.sequence',
           ],
         [ 'count' => '>0',
           'sequence1' => '<${sequence3}'
           ],
         ],

       // Logout

       ['logout', [ 'POST', '/api/logout', [], null, null ]],

       ];
  }
}