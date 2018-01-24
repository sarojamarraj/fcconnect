<?php

require_once(__DIR__ . '/CommonMacros.php');

class CustomsOrderTest extends CommonMacros
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

       [ 'look at the order to see that it is has the customs flag',
         [ 'GET', '/api/order/${order-id}' ],
         [
          'customs-survey' => 'customsSurveyFormRequired'
           ],
         [
          'customs-survey' =>  true
          ]
         ],

       [ 'update the order with a scheduled pickup and a customs invoice',
         [ 'PUT', '/api/order/${order-id}', null, self::customsPayload() ]
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

       [ 'book the order with a scheduled pickup and a customs invoice',
         [ 'POST', '/api/order/book/${order-id}', null, null ]
         ],

       [ 'look at the order to check that the customs invoice is there',
         [ 'GET', '/api/order/${order-id}' ],
         [
          'customs-invoice' => 'customsInvoice'
          ],
         [
          'customs-invoice' =>  '@notnull'
          ]
         ],

       [ 'update the customs invoice, change phone number',
         [ 'PUT', '/api/order/${order-id}/customs-invoice', null,
           [
            'contactInfo' => [
                              'phone' => '416-555-3433'
                              ],
            'products' => [ [
                             "origin" => "CA"
                             ],
                            [
                             "origin" => "CA"
                             ],
                            [
                             "description" => 'hello there',
                             "origin" =>  "US",

                             ] ]
            ] ],
         [],
         []
         ],

       [ 'update the order change scheduled pickup',
         [ 'PUT', '/api/order/${order-id}', null,
           [
            'scheduledPickup' => [
                                  "deliveryInstructions" => "let's change the instructions",
                                  "deliveryCloseTime" => '18:00'
                                  ]
            ]],
         [ 'delivery-close-time' => 'scheduledPickup.deliveryCloseTime' ],
         [ 'delivery-close-time' => '18:00' ]
         ],

       [ 'look at the order to check that the patch worked',
         [ 'GET', '/api/order/${order-id}' ],
         [
          'customs-invoice-phone' => 'customsInvoice.contactInfo.phone',
          'products-size-1' => 'customsInvoice.products.@size'
          ],
         [
          'customs-invoice-phone' =>  '416-555-3433',
          'products-size-1' => 3
          ]
         ],

       [ 'update the customs invoice, remove a product',
         [ 'PUT', '/api/order/${order-id}/customs-invoice', null,
           [
            'products' => [ [
                             "origin" => "CA"
                             ],
                            [
                             "description" => 'hello there',
                             "origin" =>  "US",

                             ] ]
            ] ],
         [],
         []
         ],

       [ 'look at the order to check that the remove worked',
         [ 'GET', '/api/order/${order-id}' ],
         [
          'products-size-2' => 'customsInvoice.products.@size'
          ],
         [
          'products-size-2' => 2
          ]
         ],

       [ 'create an order within canada',
         [ 'POST', '/api/order/create', null, self::samplePayload2(), ],
         [
          'order-id2' => 'id',
          'customer-id-order-2' => 'customer.customerId' ,
          ],
         [ 'order-id2' => '@notNull',
           'customer-id-order-2' => '${customer-id}',
           ],
         ],

       [ 'look at the order to see that it is has the customs flag',
         [ 'GET', '/api/order/${order-id2}' ],
         [
          'customs-survey2' => 'customsSurveyFormRequired'
           ],
         [
          'customs-survey2' =>  false
          ]
         ]
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

  public static function samplePayload2()
  {
    $payload = CommonMacros::samplePayload();

    $payload['shipTo'] = [
                         "company" => "Sample 2",
                         "companyName" => "Sample 3",
                         "country" => 'CA'
                          ];

    return $payload;
  }

  public static function cleanup()
  {
    return [
            [ 'logout', [ 'POST', '/api/logout' ]],
            [ 'GROUP', self::adminLogin() ],
            [ 'GROUP', self::cleanupCustomerStaff() ],
            [ 'Delete the order', [ 'DELETE', '/api/order/${order-id}' ]],
            [ 'Delete the order 2', [ 'DELETE', '/api/order/${order-id2}' ]],
            [ 'logout', [ 'POST', '/api/logout' ]],
            ];
  }

  public static function customsPayload()
  {
    $payload = json_decode(file_get_contents(__DIR__ . '/data/customs-payload.json'), true);
    $payload['id'] = '${order-id}';

    return $payload;
  }
}

