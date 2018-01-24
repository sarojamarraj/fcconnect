<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderSortInvoiceStatusTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'order by invoice status desc',
         [ 'GET', '/api/order', [ 'sort' => 'invoiceStatus,desc' ]],
         [],
         []
         ],

       [ 'order by invoice status asc',
         [ 'GET', '/api/order', [ 'sort' => 'invoiceStatus,asc' ]],
         [],
         []
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}