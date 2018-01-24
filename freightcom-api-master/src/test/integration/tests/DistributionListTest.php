<?php

require_once(__DIR__ . '/CommonMacros.php');

class DistributionListTest extends CommonMacros
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


       [ 'Upload an invalid  distribution list',
         [ 'POST', '/api/order/upload-distribution-list', null, null,
           [
            'file' => '@' . __DIR__ . '/data/distribution-error.csv',
            ] ],
         [  ],
         [  ],
         [
          'key' => 'xcontactName',
          'message' => 'Invalid column name'
          ]
         ],


       [ 'Upload a distribution list',
         [ 'POST', '/api/order/upload-distribution-list', null, null,
           [
            'file' => '@' . __DIR__ . '/data/distribution.csv',
            ] ],
         [ 'distribution-list-id' => 'id' ],
         [ 'distribution-list-id' => '@notnull' ]
         ],

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::samplePayload(), ],
         [
          'order-id' => 'id',
          'customer-id-order-1' => 'customer.customerId' ,
          ],
         [ 'order-id' => '@notNull',
           'customer-id-order-1' => '${customer-id}',
           ],
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
    $payload['distributionGroupId'] = '${distribution-list-id}';

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
}

