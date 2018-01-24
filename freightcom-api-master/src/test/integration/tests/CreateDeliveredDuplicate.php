<?php

require_once(__DIR__ . '/CommonMacros.php');

class CreateDeliveredDuplicate extends CommonMacros
{
  public static function requests()
  {
    self::clearCaches();

    return
      [
       self::customer_staff_login_request('sdaudlin', getenv('FREIGHTCOM_sdaudlin')),
       [ 'Set role to palomino inc', [ 'POST', '/api/set-session-role/17888' ] ],

       [ 'Duplicate an order',
         [ 'GET', '/api/order/duplicate/507726' ],
         [ 'order-data' => '' ],
         ],

       [ 'create an order',
         [ 'POST', '/api/order/create', null, '${order-data}', ],
         [
          'order-id' => 'id',
          ],
         [ 'order-id' => '@notNull',
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

       [ 'Logout as admin', [ 'POST', '/api/logout', [], null, null ]],

       ];
  }
}