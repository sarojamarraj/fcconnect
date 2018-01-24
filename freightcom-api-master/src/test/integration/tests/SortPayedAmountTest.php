<?php

require_once(__DIR__ . '/CommonMacros.php');

class SortPayedAmountTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'list invoices in ascending paid amount',
         [ 'GET', '/api/invoice', [ 'sort' => 'paidAmount,asc', 'size' => 100 ]],
         [ 'paid-amount' => '_embedded.invoice.*.paidAmount' ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [

       [[ 'POST', '/api/logout', [], null, null ]],


       ];
  }
}
