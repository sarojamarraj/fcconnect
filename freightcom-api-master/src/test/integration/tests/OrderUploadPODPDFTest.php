<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderUploadPODPDFTest extends CommonMacros
{
  public static function requests()
  {
    $user = self::randomUser('hex01');
    $password = self::randomPassword();

    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       [ 'GROUP', self::adminLogin() ],
       [ 'GROUP', self::createCustomerStaff($user, $password) ],

       [[ 'POST', '/api/logout', [], null, null ]],


       // As customer staff, create order
       self::customer_staff_login_request($user, $password),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'order-id' => 'id'
          ],
         [ 'order-id' => '@notNull'
           ],
         ],


       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       [ 'GROUP', self::adminLogin() ],

       [ 'list roots', [ 'GET', '/api/dm/roots' ]],

       [ 'upload pod',
         [ 'POST', '/api/order/${order-id}/upload-pod', null, null,
           [ 'file' => '@' . __DIR__ . '/data/pod.pdf',
             'foo' => 'bar',
             'john' => 'smith' ] ],
         [ 'path' => 'path' ],
         [ 'path' => '@notnull' ]
         ],

       [ 'download pod',
         [ 'GET', '/api/order/${order-id}/pod', null, null ],
         [],
         []
         ],

       [ 'check that order is delivered',
         [ 'GET', '/api/order/${order-id}' ],
         [ 'order-status' => 'orderStatus.name',
           'pod-name' => 'podName'
           ],
         [
          'order-status' => 'DELIVERED',
          'pod-name' => 'pod.pdf'
          ]
         ],

       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'Logout', [ 'POST', '/api/logout', [], null, null ]],

       [ 'GROUP', self::adminLogin() ],

       [ 'delete the order',
         [ 'DELETE', '/api/order/${order-id}'],
         [  ],
         [],
         ],

       [ 'GROUP', self::cleanupCustomerStaff() ],

       [ 'Logout', [ 'POST', '/api/logout', [], null, null ]],

       ];
  }

  static function getPayload()
  {
    return [
            'shipTo' => [ 'city' => 'Toronto' ],
            'packageTypeId' => 1,
            'packageTypeName' => 'env',
            'customerId' => '${customer-id}',
            "signatureRequired" => "Yes",
            "accessorialServices" => [
                                      "Saturday Pickup" => true,
                                      "Residential Delivery" => true,
                                      "Saturday Delivery" => true,
                                      "Hold For Pickup" => true,
                                      "Residential Pickup" => true
                                      ],
            ];
  }
}