<?php

require_once(__DIR__ . '/CommonMacros.php');

class DeletedInvoiceTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'Show deleted invoices',
         [ 'GET', '/api/deleted-invoices' ]
         ],


       [ "Logout", [ 'POST', '/api/logout', [], null, null ]],
       ];
  }


}
