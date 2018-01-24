<?php

require_once(__DIR__ . '/CommonMacros.php');

class CarrierListTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'list carriers', [ 'GET', '/api/carrier', [ 'sort' => 'name', 'size' => '99999' ]]],
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
