<?php

require_once(__DIR__ . '/CommonMacros.php');

class OrderSortTest extends CommonMacros
{
  public static function requests()
  {
    return
      [
       [ 'GROUP', self::adminLogin() ],
       // Sort tests

       [ 'list orders sort scheduled ship date',
         [ 'GET', '/api/order', [ 'sort' => 'scheduledShipDate,desc' ] ],
         [ 'ship-dates' => '_embedded.customerOrders.*.scheduledShipDate' ]
         ],

       [ 'list orders sort statusid',
         [ 'GET', '/api/order', [ 'sort' => 'statusId,asc' ] ],
         [ 'status-names' => '_embedded.customerOrders.*.statusName' ]
         ],

       [ 'list orders sort status name',
         [ 'GET', '/api/order', [ 'sort' => 'statusId,asc' ] ],
         [ 'status-names' => '_embedded.customerOrders.*.statusName' ]
         ],

       [ 'list orders sort total charge',
         [ 'GET', '/api/order', [ 'sort' => 'totalCharge,asc' ] ],
         [ 'status-names' => '_embedded.customerOrders.*.statusName' ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'logout', [ 'POST', '/api/logout', [], null, null ]]
       ];
  }
}
