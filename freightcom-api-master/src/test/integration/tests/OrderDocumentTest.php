<?php

require_once(__DIR__ . '/CommonMacros.php');

class OrderDocumentTest extends CommonMacros
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

       [ 'upload a document',
         [ 'POST', '/api/order/${order-id}/upload', null, null,
           [ 'file' => '@' . __DIR__ . '/data/pod.pdf' ] ],
         [ 'path' => 'path' ],
         [ 'path' => '@notnull' ]
         ],

       [ 'list recent orders',
         [ 'GET', '/api/order', [ 'sort' => 'id,desc'] ]
         ],

       [ 'upload another document',
         [ 'POST', '/api/order/${order-id}/upload', null, null,
           [ 'file' => '@' . __DIR__ . '/data/test.txt' ] ],
         [ 'path' => 'path' ],
         [ 'path' => '@notnull' ]
         ],

       [ 'look at the order to see that it is has the files',
         [ 'GET', '/api/order/${order-id}' ],
         [
          'file-count' => 'files.@size',
          'file-id-1' => 'files.0.id',
          'file-id-2' => 'files.1.id'
           ],
         [
          'file-count' =>  2,
          'file-id-2' => '@notnull'
          ]
         ],

       [ 'download file 2',
         [ 'GET', '/api/order/file/${file-id-2}', null, null ],
         [],
         []
         ],

       [ 'list recent orders',
         [ 'GET', '/api/order', [ 'sort' => 'id,desc'] ]
         ],

       [ 'delete file 2',
         [ 'DELETE', '/api/order/file/${file-id-1}', null, null ],
         [ 'status' => 'status' ],
         [ 'status' => 'ok' ]
         ],

       [ 'list recent orders',
         [ 'GET', '/api/order', [ 'sort' => 'id,desc'] ]
         ],

       [ 'look at the order to see that one file is deleted',
         [ 'GET', '/api/order/${order-id}' ],
         [
          'file-count-del' => 'files.@size',
          'file-id-del' => 'files.0.id'
           ],
         [
          'file-count-del' =>  1,
          'file-id-del' => '${file-id-2}'
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

  public static function customsPayload()
  {
    $payload = json_decode(file_get_contents(__DIR__ . '/data/customs-payload.json'), true);
    $payload['id'] = '${order-id}';

    return $payload;
  }
}

