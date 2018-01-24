<?php

require_once(__DIR__ . '/CommonMacros.php');

class CommissionsPayableTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'list payabales', [ 'GET', '/api/commissionstatement' ]],
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
