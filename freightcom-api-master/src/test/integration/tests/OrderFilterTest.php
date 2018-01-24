<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderFilterTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'filter by multiple package type ids',
         [ 'GET', '/api/order', [ 'packagetypeid' => '1,2' ]],
         [],
         []
         ],

       [ 'filter by one package type id',
         [ 'GET', '/api/order', [ 'packagetypeid' => '1' ]],
         [],
         []
         ],

       [ 'filter by multiple package type ids',
         [ 'GET', '/api/order', [ 'packagetypeid' => '1,2a' ]],
         [],
         [],
         "Invalid package type id"
         ],


       [ 'filter by multiple status name',
         [ 'GET', '/api/order', [ 'orderStatusName' => 'DELIVERED,   READY FOR SHIPPING' ]],
         [],
         []
         ],

       [ 'filter by one status name',
         [ 'GET', '/api/order', [ 'orderStatusName' => 'DRAFT' ]],
         [],
         []
         ],

       [ 'filter by multiple status ids',
         [ 'GET', '/api/order', [ 'statusId' => '1,3' ]],
         [],
         []
         ],

       [ 'filter by one status id',
         [ 'GET', '/api/order', [ 'statusId' => '1' ]],
         [],
         []
         ],

       [ 'filter by one status id',
         [ 'GET', '/api/order', [ 'status' => '1' ]],
         [],
         []
         ],

       [ 'filter by multiple status ids',
         [ 'GET', '/api/order', [ 'statusId' => '1,3a' ]],
         [],
         [],
         "Invalid order status id"
         ],

       [ 'filter by multiple carrier names',
         [ 'GET', '/api/order', [ 'carrierName' => 'Purolator Courier,Fedex' ]],
         [],
         []
         ],

       [ 'filter by one carrier name',
         [ 'GET', '/api/order', [ 'carrierName' => 'Fedex' ]],
         [],
         []
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}