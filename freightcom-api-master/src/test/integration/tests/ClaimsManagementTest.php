<?php

require_once(__DIR__ . '/CommonMacros.php');

class ClaimsManagementTest extends CommonMacros
{
  public static function requests()
  {
    self::clearCaches();

    return
      [

       [ 'GROUP', self::adminLogin() ],
       [ 'GROUP', self::createCustomerStaff(self::randomUser('staff-a'), self::randomPassword('staff-a')) ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       // Test as customer staff

       self::customer_staff_login_request(self::randomUser('staff-a'), self::randomPassword('staff-a')),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::samplePayload(), ],
         [
          'order-id' => 'id',
          'customer-id-order-1' => 'customer.customerId' ,
          ],
         [ 'order-id' => '@notNull',
           'customer-id-order-1' => '${customer-id}',
           ],
         ],

       [ 'select a service',
         [ 'GET', '/api/service', [ 'size' => 2 ]],
         [ 'service-id' => '_embedded.services.1.id' ],
         [ 'service-id' => '@notnull' ],
         ],

       [ 'select a quote',
         [ 'PUT', '/api/order/test-rate/${service-id}/${order-id}', null, [ 'base' => 200 ]],
         [],
         [],
         ],

       [ 'book the order',
         [ 'POST', '/api/order/book/${order-id}' ],
         [],
         [],
         ],


       [[ 'POST', '/api/logout', [], null, null ]],

       [ 'GROUP', self::adminLogin() ],

       [ 'deliver the order',
         [ 'POST', '/api/order/${order-id}/mark-as-delivered' ],
         [],
         [],
         ],

       [ 'create an invoice',
         [ 'POST', '/api/generate-invoice', null, [
                                                   'orders' => [ [ 'id' => '${order-id}',]],
                                                   'customer' => [ 'id' => '${customer-id}' ],
                                                   ],
           ],
         [ 'invoice-id' => 'id' ],
         [ 'invoice-id' => '@notnull' ],
         ],

       [ 'List orders with claims',
         [ 'GET',  '/api/orders-with-claims'  ]
         ],

       [ 'Logout as admin', [ 'POST', '/api/logout', [], null, null ]],

       // Create claim as customer staff

       self::customer_staff_login_request(self::randomUser('staff-a'), self::randomPassword('staff-a')),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'Submit a claim',
         [ 'POST', '/api/order/${order-id}/submit-claim', null, [ 'comment' => 'They broke my ming vase' ]],
         [ 'status' => 'status' ],
         [ 'status' => 'ok' ]
         ],

       [ 'List orders with claims as customer staff',
         [ 'GET',  '/api/orders-with-claims', [ 'sort' => 'claim.ageInDays', 'customerid' => '${customer-id}' ]  ],
         [ 'number-claims' => '_embedded.customerOrders.@size' ],
         [ 'number-claims' => '@null' ],
         [ 'key' => 'message', 'message' => 'Not authorized' ]
         ],

       [ 'GROUP', self::adminLogin() ],

       [ 'List orders with claims as admin',
         [ 'GET',  '/api/orders-with-claims', [ 'sort' => 'claim.ageInDays', 'customerid' => '${customer-id}' ] ],
         [
          'number-claims' => '_embedded.customerOrders.@size'
          ],
         [
          'number-claims' => '>0'
          ]
         ],

       [ 'List the order status messages',
         [ 'GET', '/api/order/${order-id}/status-messages' ]
         ],

       [ 'Update the claim',
         [ 'PUT', '/api/order/${order-id}/update-claim', null,
           [
            'comment' => 'I dispute that',
            'amount' => '33.12',
            'status' => 'REVIEWED']
           ]],

       [ 'List the order status messages again',
         [ 'GET', '/api/order/${order-id}/status-messages' ]
         ],

       [ 'Show the order with claim info',
         [ 'GET', '/api/order/${order-id}' ],
         [ 'claim-id' => 'claim.id' ],
         [ 'claim-id' => '@notnull' ]
         ],

       [ 'Update the claim again',
         [ 'PUT', '/api/order/${order-id}/update-claim', null,
           [
            'comment' => 'Well, we can see your point',
            'status' => 'IN-PROCESS'
            ]]
         ] ,

       [ 'List orders with claims as admin check amount and status',
         [ 'GET',  '/api/orders-with-claims', [ 'sort' => 'claim.ageInDays', 'customerid' => '${customer-id}' ]  ],
         [ 'number-claims-2' => '_embedded.customerOrders.@size',
           'amount' => '_embedded.customerOrders.0.claimAmount'
           ],
         [ 'number-claims-2' => '@notnull',
           'amount' => '33.12'
           ]
         ],
       ];
  }

  public static function samplePayload()
  {
    $payload = CommonMacros::samplePayload();

    $payload['shipTo'] = [
                         "company" => "CCCC BBBBB",
                         "companyName" => "CCCC BBBBB",
                         "country" => 'US'
                          ];

    return $payload;
  }

  public static function cleanup()
  {
    return [
            [ 'logout', [ 'POST', '/api/logout' ]],
            [ 'GROUP', self::adminLogin() ],
            [ 'Really Delete the invoice', [ 'DELETE', '/api/invoice/${invoice-id}', ]],
            [ 'Delete the order', [ 'DELETE', '/api/order/${order-id}' ]],
            [ 'GROUP', self::cleanupCustomerStaff() ],
            [ 'logout', [ 'POST', '/api/logout' ]],
            ];
  }
}

