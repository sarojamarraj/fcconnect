<?php

require_once(__DIR__ . '/CommonMacros.php');

class UnbilledChargesTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],


       [ 'list orders with unbilled charges',
         [ 'GET', '/api/submitted-orders',
           [ 'invoiceStatus' => 'unbilled charges',
             'sort' => 'id,asc']]
         ],
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
