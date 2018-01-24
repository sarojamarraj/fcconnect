<?php

require_once(__DIR__ . '/CommonMacros.php');

class CustomsSearchTest extends CommonMacros
{
  public static function requests()
  {
    self::clearCaches();

    return
      [
       [ 'GROUP', self::adminLogin() ],

       [ 'Get customers by name',
         [ 'GET', '/api/customer/search/byName', [ 'name' => 'pal' ]]
         ]
       ];
  }

  public static function cleanup()
  {
    return [
            [ 'logout', [ 'POST', '/api/logout' ]],
            ];
  }
}

