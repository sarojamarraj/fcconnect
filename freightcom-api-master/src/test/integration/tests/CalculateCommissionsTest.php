<?php

require_once(__DIR__ . '/CommonMacros.php');

class CalculateCommissionsTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       // [ 'list payabales', [ 'PUT', '/api/calculate-commissions' ]],
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
