<?php

require_once(__DIR__ . '/CommonMacros.php');

class WorkOrderQuoteTest extends CommonMacros
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

       [ 'create a work  order',
         [ 'POST', '/api/order/create', null, array_merge(self::samplePayload(), [ 'customWorkOrder' => true ]), ],
         [
          'order-id' => 'id',
          'customer-id' => 'customer.customerId' ,
          ],
         [ 'order-id' => '@notNull',
           'customer-id' => '@notNull',
           ],
         ],

       [ 'ask for a quote for the order',
         [ 'POST', '/api/order/send-quote/${order-id}' ],
         [],
         [],
         ],

       [
        'list work orders',
        [ 'GET', '/api/order', [ 'customworkorder' => true,
                                 'customerId' => '${customer-id}' ] ],
        [ 'ship-from-email-notification-enabled'
          => '_embedded.customerOrders.0.shipFrom.emailNotification' ],
        [ 'ship-from-email-notification-enabled' => true ]
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

