<?php

require_once(__DIR__ . '/CommonMacros.php');


class CountTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       // Months

       ['get order counts forever',
        [ 'GET', '/api/orders-by-month', [ ], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       ['get order counts 2016',
        [ 'GET', '/api/orders-by-month', [ 'from' => '2016-01-01', 'to' => '2016-12-31'], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       //

       ['get order counts for customer 6539',
        [ 'GET', '/api/orders-by-month', [ 'from' => '2016-01-01', 'to' => '2016-12-31', 'customerId' => 6539 ], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       //

       ['get order counts for agent 9657',
        [ 'GET', '/api/orders-by-month', [ 'from' => '2016-01-01', 'to' => '2016-12-31', 'agentId' => 9657 ], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       // Weeks

       ['get order counts 2016',
        [ 'GET', '/api/orders-by-week', [ 'from' => '2016-01-01', 'to' => '2016-12-31'], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       //

       ['get order counts for customer 6539',
        [ 'GET', '/api/orders-by-week', [ 'from' => '2016-01-01', 'to' => '2016-12-31', 'customerId' => 6539 ], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       //

       ['get order counts for agent 9657',
        [ 'GET', '/api/orders-by-week', [ 'from' => '2016-01-01', 'to' => '2016-12-31', 'agentId' => 9657 ], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       // By Day

       ['get order counts 2016',
        [ 'GET', '/api/orders-by-day', [ 'from' => '2016-01-01', 'to' => '2016-12-31'], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       //

       ['get order counts for customer 6539',
        [ 'GET', '/api/orders-by-day', [ 'from' => '2016-01-01', 'to' => '2016-12-31', 'customerId' => 6539 ], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       //

       ['get order counts for agent 9657',
        [ 'GET', '/api/orders-by-day', [ 'from' => '2016-01-01', 'to' => '2016-12-31', 'agentId' => 9657 ], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       //

       ['get order counts for agent 9657',
        [ 'GET', '/api/orders-by-day', [ 'from' => '2016-08-01', 'to' => '2016-12-31', 'agentId' => 9657, 'customerId' => 8646 ], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}