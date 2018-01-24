<?php

require_once(__DIR__ . '/CommonMacros.php');

class CreateAdminTest extends CommonMacros
{
  public static function requests()
  {
    $password = substr(md5(mt_rand()), 0, 18);

    return
      [
       self::customer_login_request('admin'),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'Lookup john grool',
         [ 'GET', '/api/user', [ 'login' => 'john.grool' ] ],
         [ 'old-john-id' => '_embedded.user.0.id' ]
         ],

       [ 'CONDITION', [ 'old-john-id' => '@null' ], 'DO_NOT_DELETE' ],

       [ 'Delete john grool',
         [ 'DELETE', '/api/user/${old-john-id}' ]
         ],

       [ 'LABEL', 'DO_NOT_DELETE' ],

       [
        'Create a customer admin',
        [ 'POST', '/api/user/customer_admin/10728', null,
          [
           'login' => 'john.grool',
           'password' => $password,
           'firstname' => 'John',
           'lastname' => 'Grool',
           'email' => 'john.grool@palominosys.com',
           ]],
        [ 'customer-user-login' => 'login',
          'customer-admin-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       [ 'make it an admin',
         [ 'POST', '/api/user/role/admin', null, [ 'userId' => '${customer-admin-id}' ]]
         ],

       [ 'Logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}
