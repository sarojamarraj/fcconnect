<?php

require_once(__DIR__ . '/CommonMacros.php');

class SubmittedOrderTest extends CommonMacros
{
  public static function requests()
  {
    self::clearCaches();

    return
      [
       [ 'GROUP', self::adminLogin() ],

       [ 'Get submitted orders',
         [ 'GET', '/api/submitted-orders', [ 'sort' => 'id,desc', 'size' => 20 ]]
         ]
       ];
  }

  public static function cleanup()
  {
    return [
            [ 'logout', [ 'POST', '/api/logout' ]]
            ];
  }
}

