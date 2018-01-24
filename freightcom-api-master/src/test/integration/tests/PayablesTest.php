<?php

require_once(__DIR__ . '/CommonMacros.php');

class PayablesTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'list payabales',
         [ 'GET', '/api/payablestatement', [ 'sort' => 'service.name' ] ]],
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
