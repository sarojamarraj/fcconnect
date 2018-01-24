<?php

require_once(__DIR__ . '/CommonMacros.php');

class AdminViewInvoiceTest extends CommonMacros
{
  public static $referenceNumber;
  public static $referenceNumber2;
  public static $trackingNumber;

  public static function requests()
  {

    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       // View specific invoice as admin

       [ 'view an invoice',
         [ 'POST', '/api/invoice/view/111470',],
         [ 'viewed-at' => 'viewedAt' ],
         [ 'viewed-at' => '@notnull' ],
         ],
       ];
  }

  public static function cleanup()
  {
    return
      [
       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       ];
  }
}
