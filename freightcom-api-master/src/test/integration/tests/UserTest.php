<?php

require_once(__DIR__ . '/CommonMacros.php');

class UserTest extends CommonMacros
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
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name) ],
        [ 'customer-id' => 'id' ],
        []
        ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [ 'login' => $customer_user_name,
                                                                     'firstname' => $rand_first,
                                                                     'lastname' => $rand_last,
                                                                     'email' => self::generateEmail(),
                                                                     'password' => $password ]],
        [ 'customer-user-login' => 'login',
          'customer-staff-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       [
        'Create a customer admin',
        [ 'POST', '/api/user/customer_admin/${customer-id}', null, [ 'login' => $customer_admin_name,
                                                                     'firstname' => 'George',
                                                                     'lastname' => 'Alexandro',
                                                                     'password' => $password,
                                                                     'email' => self::generateEmail(), ]],
        [ 'customer-admin-login' => 'login',
          'customer-admin-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        [ 'customer-admin-login' => $customer_admin_name, ]
        ],


       [ 'find user by part of first name', [ 'GET', '/api/user', [ 'name' => $firstname_comp ]],
         [
          'test-count' => '_embedded.user.@size',
          'test-first' => '_embedded.user.0.firstname',
          'test-last' => '_embedded.user.0.lastname',
          ],
         [
          'test-count' => 1,
          'test-first' => $rand_first,
          'test-last'  => $rand_last,
           ],
         ],


       [ 'find user by part of first and last name', [ 'GET', '/api/user', [ 'name' => "$firstname_comp last" ]],
         [
          'test-count' => '_embedded.user.@size',
          'test-first' => '_embedded.user.0.firstname',
          'test-last' => '_embedded.user.0.lastname',
          ],
         [
          'test-count' => 1,
          'test-first' => $rand_first,
          'test-last'  => $rand_last,
           ],
         ],


       [ 'find user with admin role', [ 'GET', '/api/user', [ 'role' => 'admin' ]],
         [ 'authorities' => '_embedded.user.*.authorities' ],
         [ 'authorities' => function($authorities) {
             return all(function($roles) { return some(function($role) { return $role['roleName'] == 'ADMIN'; }, $roles);  },
                        $authorities);
           }
           ],
         ],

       [ 'customer staff role', [ 'GET', '/api/user', [ 'role' => 'customer_staff' ] ],
         [ 'authorities' => '_embedded.user.*.authorities' ],
         [ 'authorities' => function($authorities) {
             return all(function($roles) { return some(function($role) { return $role['roleName'] == 'CUSTOMER_STAFF'; }, $roles);  },
                        $authorities);
           }
           ],
         ],

       [ 'customer admin role', [ 'GET', '/api/user', [ 'role' => 'customer_admin' ] ],
         [ 'authorities' => '_embedded.user.*.authorities' ],
         [ 'authorities' => function($authorities) {
             return all(function($roles) { return some(function($role) { return $role['roleName'] == 'CUSTOMER_ADMIN'; }, $roles);  },
                        $authorities);
           }
           ],
         ],

       [ 'freightcom staff', [ 'GET', '/api/user', [ 'role' => 'freightcom_staff' ] ],
         [ 'authorities' => '_embedded.user.*.authorities' ],
         [ 'authorities' => function($authorities) {
             return all(function($roles) { return some(function($role) { return $role['roleName'] == 'FREIGHTCOM_STAFF'; }, $roles);  },
                        $authorities);
           }
           ],
         ],
       [ 'agent', [ 'GET', '/api/user', [ 'role' => 'agent' ] ],
         [ 'authorities' => '_embedded.user.*.authorities' ],
         [ 'authorities' => function($authorities) {
             return all(function($roles) { return some(function($role) { return $role['roleName'] == 'AGENT'; }, $roles);  },
                        $authorities);
           }
           ],
         ],
       [ 'have customer 4112', [ 'GET', '/api/user', [ 'customer_id' => '4112' ] ],
         [ 'authorities' => '_embedded.user.*.authorities' ],
         [ 'authorities' => function($authorities) {
             return all(function($roles) { return some(function($role) { return isset($role['customerId']) && $role['customerId'] == 4112; }, $roles);  },
                        $authorities);
           }
           ],
         ],
       [ 'login like admin and user is active', [ 'GET', '/api/user', [ 'enabled' => 'active', 'login' => 'admin' ] ],
         [ 'enabled' => '_embedded.user.*.enabled',
           'logins' => '_embedded.user.*.login', ],
         [ 'enabled' => function($enabled) {
             return all(function($flag) {
                 return $flag; },
               $enabled);
           },
           'logins' => function($logins) {
             return all(function($login) {
                 return preg_match('/admin/ui', $login);
               },
               $logins);
           },
           ],
         ],
       [ 'active', [ 'GET', '/api/user', [ 'enabled' => 'active' ] ],
         [ 'enabled' => '_embedded.user.*.enabled' ],
         [ 'enabled' => function($enabled) {
             return all(function($flag) {
                 return $flag; },
               $enabled);
           }
           ],
         ],
       [ 'inactive', [ 'GET', '/api/user', [ 'enabled' => 'inactive' ] ],
         [ 'enabled' => '_embedded.user.*.enabled' ],
         [ 'enabled' => function($enabled) {
             return all(function($flag) { return ! $flag; },
                        $enabled);
           }
           ],
         ],
       [ 'active 2', [ 'GET', '/api/user', [ 'enabled' => 'true' ] ],
         [ 'enabled' => '_embedded.user.*.enabled' ],
         [ 'enabled' => function($enabled) {
             return all(function($flag) {
                 return $flag; },
               $enabled);
           }
           ],
         ],
       [ 'inactive 2', [ 'GET', '/api/user', [ 'enabled' => 'false' ] ],
         [ 'enabled' => '_embedded.user.*.enabled' ],
         [ 'enabled' => function($enabled) {
             return all(function($flag) { return ! $flag; },
                        $enabled);
           }
           ],
         ],

       [
        'Delete the customer staff',
        [ 'DELETE', '/api/user/${customer-staff-id}' ],
        [],
        []
        ],

       [
        'Delete the customer admin',
        [ 'DELETE', '/api/user/${customer-admin-id}' ],
        [],
        []
        ],

       [
        'Delete the customer',
        [ 'DELETE', '/api/customer/${customer-id}' ],
        [],
        []
        ],



       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}