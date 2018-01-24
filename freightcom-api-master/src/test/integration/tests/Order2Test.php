<?php

require_once(__DIR__ . '/CommonMacros.php');

class Order2Test extends CommonMacros
{
  public static $shipDate;

  public static function requests()
  {
    $customer_name = 'customer_name' . substr(md5(mt_rand()), 0, 8);
    $customer_user_name = 'customer_user_name' . substr(md5(mt_rand()), 0, 8);
    $password = substr(md5(mt_rand()), 0, 18);
    $password2 = substr(md5(mt_rand()), 0, 18);

    $date = new DateTime();
    $date->setTimeZone(new DateTimeZone('UTC'));
    $date->modify('+1 day');

    self::$shipDate = $date->format('Y-m-d');

    $date->modify('-1 month');
    $fromDate = $date->format('Y-m-d');

    $date->modify('+2 month');
    $toDate = $date->format('Y-m-d');

    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name) ],
        [ 'customer-id' => 'id' ],
        []
        ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [ 'login' => $customer_user_name,
                                                                     'password' => $password,
                                                                     'email' => self::generateEmail(),
                                                                     'firstname' => 'Pierre',
                                                                     'lastname' => 'Pettigrew'
                                                                     ]],
        [ 'customer-user-login' => 'login',
          'customer-user-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       // Log in as customer

       self::customer_staff_login_request($customer_user_name, $password),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'new-order' => '',
          'order-id' => 'id',
          'id1' => 'id',
          'customer-id' => 'customer.customerId' ,
          ],
         [ 'order-id' => '@notNull',
           'customer-id' => '@notNull',
           ],
         ],

       [ 'Let\'s change the password',
         [ 'PUT', '/api/user/${customer-user-id}', null, [ 'password' => $password2 ]],
         [],
         [],
         ],

       [[ 'POST', '/api/logout', [], null, null ]],

       // Login with new password
       self::customer_staff_login_request($customer_user_name, $password2),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       // Check histograms as customer

       ['get order counts as customer',
        [ 'GET', '/api/orders-by-month', [ 'from' => $fromDate, 'to' => $toDate ], ],
        [
         'histogram' => ''
         ],
        [ 'histogram' => function($histogram) {
            return reduce(function($bucket, $count) {
                return $count + $bucket['count'];
              }, $histogram, 0) == 1;
          },
          ]],

       ['get order counts as customer',
        [ 'GET', '/api/orders-by-week', [ 'from' => $fromDate, 'to' => $toDate ], ],
        [
         'histogram' => ''
         ],
        [ 'histogram' => function($histogram) {
            return reduce(function($bucket, $count) {
                return $count + $bucket['count'];
              }, $histogram, 0) == 1;
          },
          ]],

       ['get order counts as customer',
        [ 'GET', '/api/orders-by-day', [ 'from' => $fromDate, 'to' => $toDate ], ],
        [
         'histogram' => ''
         ],
        [ 'histogram' => function($histogram) {
            return reduce(function($bucket, $count) {
                return $count + $bucket['count'];
              }, $histogram, 0) == 1;
          },
          ]],

       ['check error handling on counts',
        [ 'GET', '/api/orders-by-day', [ 'from' => $fromDate, 'to' => $toDate, 'customerId' => '3343' ], ],
        [
         ],
        [],
        "Access is denied" ],

       [[ 'POST', '/api/logout', [], null, null ]],

       // Try to fetch order as admin
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Search by customer id',
        [ 'GET', '/api/order/search/findByCustomerId', [ 'id' => '${customer-id}' ], ],
        [],
        [],
        ],

       [
        'customerorder route',
        [ 'GET', '/api/customerorder', [ 'size' => '2' ], ],
        [],
        [],
        ],

       [
        'Try to fetch order as admin',
        [ 'GET', '/api/order/${order-id}' ],
        [],
        [],
        ],

       // Check histograms as admin

       ['get order counts for customer',
        [ 'GET', '/api/orders-by-month', [ 'from' => $fromDate, 'to' => $toDate, 'customerId' => '${customer-id}' ], ],
        [
         'histogram' => ''
         ],
        [ 'histogram' => function($histogram) {
            return reduce(function($bucket, $count) {
                return $count + $bucket['count'];
              }, $histogram, 0) == 1;
          },
          ]],

       ['get order counts for customer',
        [ 'GET', '/api/orders-by-week', [ 'from' => $fromDate, 'to' => $toDate, 'customerId' => '${customer-id}' ], ],
        [
         'histogram' => ''
         ],
        [ 'histogram' => function($histogram) {
            return reduce(function($bucket, $count) {
                return $count + $bucket['count'];
              }, $histogram, 0) == 1;
          },
          ]],

       ['get order counts for customer',
        [ 'GET', '/api/orders-by-day', [ 'from' => $fromDate, 'to' => $toDate, 'customerId' => '${customer-id}' ], ],
        [
         'histogram' => ''
         ],
        [ 'histogram' => function($histogram) {
            return reduce(function($bucket, $count) {
                return $count + $bucket['count'];
              }, $histogram, 0) == 1;
          },
          ]],

       // Clean up

       [
        'Delete the order',
        [ 'DELETE', '/api/order/${order-id}' ],
        [],
        []
        ],

       [
        'Delete the customer staff role if not deleted',
        [ 'DELETE', '/api/user/role/${role-id}' ],
        [],
        []
        ],

       [
        'Delete the customer staff',
        [ 'DELETE', '/api/user/${customer-user-id}' ],
        [],
        []
        ],

       [
        'Delete the customer',
        [ 'DELETE', '/api/customer/${customer-id}' ],
        [],
        []
        ],



       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }

  public static function getPayload()
  {
    return [
            'shipFrom' => [ 'city' => 'A' ],
            'packageTypeId' => 1,
            'packageTypeName' => 'env',
            'customerId' => '${customer-id}',
            "scheduledShipDate" => self::$shipDate,
            ];
  }
}