<?php

require_once(__DIR__ . '/CommonMacros.php');

class SortAmountRemainingTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'list invoices in descending amount remaining',
         [ 'GET', '/api/invoice', [ 'sort' => 'amountRemaining,desc', 'size' => 100 ]],
         [ 'amount-remaining' => '_embedded.invoice.*.amountRemaining' ]
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
