<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderColorCodeTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       [ 'GROUP', self::adminLogin() ],

       [ 'Get orders with red, orange',
         [ 'GET', '/api/job-board', [
                                     'colorCodes' => 'RED,ORANGE',
                                     'size' => 10,
                                     'sort' => 'id,desc'
                                     ],
           ],
         [
          'order' => '_embedded.customerOrders.0',
          'order-id' => '_embedded.customerOrders.0.id',
          ],
         []],

       [ 'Sort orders order by code',
         [ 'GET', '/api/job-board', [
                                     'sort' => 'colorCode,desc',
                                     'size' => 4
                                     ],
           ],
         [
          'order' => '_embedded.customerOrders.0',
          'order-id' => '_embedded.customerOrders.0.id',
          ],
         []]
       ];
  }

  public static function cleanup()
  {
    return [
            // Logout

            [[ 'POST', '/api/logout', [], null, null ]],
            ];
  }
}