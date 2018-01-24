<?php

require_once(__DIR__ . '/CommonMacros.php');

class Issue282Test extends CommonMacros
{
  public static function requests()
  {
    $unique = substr(md5(mt_rand()), 0, 10);
    $customer_name = 'customer_name' . $unique . "_test";

    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Job board query',
        [ 'GET', '/api/submitted-orders', [
                                           'agentId' => '12288',
                                           'latestComment' => 'test',
                                           'shipDate' => '2017-04-06',
                                           'carrierName' => 1,
                                           'customerId' => 1,
                                           'bolId' => 5,
                                           'serviceName' => 'test',
                                           'deliveryDate' => '2017-04-06',
                                           'statusId' => "1,3",
                                           'page' => 0,
                                           'size' => 10,
                                           'sort' => 'bolId,DESC'
                                           ] ]
        ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ "logout", [ 'POST', '/api/logout', [], null, null ]],


       ];
  }
}
