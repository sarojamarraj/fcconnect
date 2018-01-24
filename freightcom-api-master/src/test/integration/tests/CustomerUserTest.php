<?php

require_once(__DIR__ . '/CommonMacros.php');

class CustomerUserTest extends CommonMacros
{
  public static function requests()
  {
    $customer_name = 'customer_name' . substr(md5(mt_rand()), 0, 8);
    $customer_user_name = 'customer_user_name' . substr(md5(mt_rand()), 0, 8);
    $customer_admin_name = 'customer_admin_name' . substr(md5(mt_rand()), 0, 8);
    $password = substr(md5(mt_rand()), 0, 18);

    $firstname_comp = substr(md5(mt_rand()), 0, 8);
    $rand_first = 'first' . $firstname_comp;
    $rand_last = 'last' . substr(md5(mt_rand()), 0, 8);

    return
      [
       self::customer_login_request(),
       self::login_lookup_request('CUSTOMER_ADMIN'),
       self::$login_set_role_request,

       [ "test listing users",
         [ 'GET', '/api/user', [ 'sort' => 'id,DESC',
                                 'page' => 0,
                                 'size' => 10 ],
           ],
         ],


       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}