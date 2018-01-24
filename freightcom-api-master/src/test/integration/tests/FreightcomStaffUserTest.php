<?php

require_once(__DIR__ . '/CommonMacros.php');

class FreightcomStaffUserTest extends CommonMacros
{
  public static function requests()
  {

    $password = substr(md5(mt_rand()), 0, 18);
    $login = substr(md5(mt_rand()), 0, 18);
    $email = substr(md5(mt_rand()), 0, 18);

    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [ 'create a freightcom staff',
         [ 'POST', '/api/user/freightcom_staff', null, [
                                                        'login' => $login,
                                                        'email' => $email,
                                                        'firstname' => 'Sarah',
                                                        'lastname' => 'Langley',
                                                        'password' => $password,
                                                        ]],
         [
          'staff-user-id' => 'id',
          'user-login' => 'login',
          'user-role' => 'authorities.0.roleName',
          'user-role-id' => 'authorities.0.id',
          ],
         [
          'user-role' => 'FREIGHTCOM_STAFF',
          'user-login' => $login,
          ],
         ],


       [
        'Update freightcom staff properties  ',
        [ 'PUT', '/api/user/role/freightcom_staff', null, [
                                                           'id' => '${user-role-id}',
                                                           'userId' => '${staff-user-id}',
                                                           'canManageDisputes' => true,
                                                           'canManageClaims' => true
                                                           ]],
        [
          'can-manage-claims' => 'authorities.0.canManageClaims',
          'can-manage-disputes' => 'authorities.0.canManageDisputes'
          ],
        [
          'can-manage-claims' => true,
          'can-manage-disputes' => true
          ],
        ],


       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],

       // Login as freightcom staff



       self::customer_staff_login_request($login, $password),
       self::login_lookup_request('FREIGHTCOM_STAFF'),
       self::$login_set_role_request,


       [ "test listing users",
         [ 'GET', '/api/user', [ 'sort' => 'id,DESC',
                                 'page' => 0,
                                 'size' => 10 ],
           ],
         ]
       ];
  }

  public static function cleanup()
  {
    return [

            // Logout

            [[ 'POST', '/api/logout', [], null, null ]],

            self::admin_login_request(),
            self::login_lookup_request('ADMIN'),
            self::$login_set_role_request,

            [
             'Delete the freightcom staff',
             [ 'DELETE', '/api/user/${staff-user-id}' ],
             [],
             []
             ],

            // Logout

            [[ 'POST', '/api/logout', [], null, null ]],

            ];
  }
}