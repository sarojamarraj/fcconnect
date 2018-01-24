<?php

require_once(__DIR__ . '/CommonMacros.php');

class CancelOrderTest extends CommonMacros
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
          'customer-id' => 'customer.customerId' ,
          ],
         [ 'order-id' => '@notNull',
           'customer-id' => '@notNull',
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

       [ 'cancel the order', [ 'PUT', '/api/order/${order-id}/cancel' ] ],

       [ 'look at the order to see that it is cancelled',
         [ 'GET', '/api/order/${order-id}' ],
         [ 'status-name' => 'orderStatus.name',
           'cancelled-charges-size' => 'charges.@size' ],
         [
          'status-name' => 'CANCELLED',
          'cancelled-charges-size' => 0
          ]
         ]
       ];
  }

  public static function cleanup()
  {
    return [
            [ 'logout', [ 'POST', '/api/logout' ]],
            [ 'GROUP', self::adminLogin() ],
            [ 'GROUP', self::cleanupCustomerStaff() ],
            [ 'Delete the order', [ 'DELETE', '/api/order/${order-id}' ]],
            [ 'logout', [ 'POST', '/api/logout' ]],
            ];
  }
}

