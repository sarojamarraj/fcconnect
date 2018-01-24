<?php

/**
 * Simple test, just list orders once
 */

require_once(__DIR__ . '/CommonMacros.php');


class AdminListOrders extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       // Get orders

       [ 'List orders', [ 'GET', '/api/order', [ 'sort' => 'id,desc'], ],
         [
          'size' => '_embedded.customerOrders.@size',
          'carrier-ids-1' => '_embedded.customerOrders.*.carrier.id',
          ],
         [
          'size' => '>0',
          ]],

       [ 'Search orders two carrier names',
         [ 'GET', '/api/order', [ 'carrierName' => 'fedex,dhl', 'sort' => 'actualCarrierName,desc', 'size' => 300 ], ],
         [ 'carrier-names' => '_embedded.customerOrders.*.carrierName' ]
         ],

       [ 'Search orders carrier name dhl',
         [ 'GET', '/api/order', [ 'carrierName' => 'dhl'], ],
         [ 'carrier-names-1' => '_embedded.customerOrders.*.carrierName' ]
         ],

       [ 'Search orders carrier id',
         [ 'GET', '/api/order', [ 'carrierid' => '27,21', 'sort' => 'id,desc'], ],
         [ 'carrier-names-2' => '_embedded.customerOrders.*.carrierName' ]
         ],

       [ 'Search orders carrier id',
         [ 'GET', '/api/order', [ 'carrierid' => '21', 'sort' => 'id,desc'], ],
         [ 'carrier-names-3' => '_embedded.customerOrders.*.carrierName' ]
         ],

       [ 'Search orders carrier id error',
         [ 'GET', '/api/order', [ 'carrierid' => '27a,3', 'sort' => 'id,desc'], ],
         [ 'carrier-names-4' => '_embedded.customerOrders.*.carrierName' ],
         null,
         "Invalid id parameter: 27a"
         ],

       [ 'get some submitted orders as admin',
         [ 'GET', '/api/submitted-orders', [
                                            'page' => 0,
                                            'size' => 10,
                                            'sort' => 'id,desc',
                                            'currency' => 'CAD'
                                            ] ],
         [ 'size' => '_embedded.order.@size' ],
         [  ],
         ],

       [ 'get some submitted orders as admin',
         [ 'GET', '/api/order', [
                                 'page' => 0,
                                 'size' => 10,
                                 'sort' => 'id,desc',
                                 'referencenum' => '002-7554067-0808239,test contact name',
                                 'statusId' => '1,3,16'
                                 ] ],
         [ 'reference-code-matches' => '_embedded.customerOrders.@size' ],
         [  ],
         ],

       [ 'get some submitted orders as admin',
         [ 'GET', '/api/order', [
                                 'page' => 0,
                                 'size' => 10,
                                 'sort' => 'id,desc',
                                 'trackingnumber' => '847037284, 401012662'
                                 ] ],
         [ 'tracking-number-matches' => '_embedded.customerOrders.@size' ],
         [  ],
         ],

       [ 'get some disputed orders as admin',
         [ 'GET', '/api/orders-with-disputes', [
                                                'page' => 0,
                                                'size' => 10,
                                                'sort' => 'id,desc'
                                                ] ],
         [ 'disputed-number-matches' => '_embedded.customerOrders.@size' ],
         [  ],
         ],

       [ 'get order statistics',
         [ 'GET', '/api/order/stats-per-type' ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }

}