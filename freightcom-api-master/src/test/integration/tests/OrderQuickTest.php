<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderQuickTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       [ 'GROUP', self::adminLogin() ],

       // Get orders

       [ 'Get orders quick', [ 'GET', '/api/order-quick', [ 'sort' => 'id,desc'], ],
         [
          'order' => '_embedded.customerOrders.0',
          'order-id' => '_embedded.customerOrders.0.id',
          ],
         []],

       // Get orders with claims

       [ 'Get orders with claims', [ 'GET', '/api/orders-with-claims', [ 'sort' => 'id,desc'], ],
         [
          'order' => '_embedded.customerOrders.0',
          'order-id' => '_embedded.customerOrders.0.id',
          ],
         []],
       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}