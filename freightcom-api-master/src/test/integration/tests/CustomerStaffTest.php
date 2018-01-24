<?php

require_once(__DIR__ . '/CommonMacros.php');

class CustomerStaffTest extends CommonMacros
{
  public static function requests()
  {
    $customer_name = 'customer_name' . substr(md5(mt_rand()), 0, 8);
    $customer_user_name = 'customer_user_name' . substr(md5(mt_rand()), 0, 8);
    $customer_admin_name = 'customer_admin_name' . substr(md5(mt_rand()), 0, 8);
    $password1 = substr(md5(mt_rand()), 0, 18);
    $password2 = substr(md5(mt_rand()), 0, 18);
    $password3 = substr(md5(mt_rand()), 0, 18);
    $password4 = substr(md5(mt_rand()), 0, 18);
    $customer2_name = 'customer_2_ name' . substr(md5(mt_rand()), 0, 8);
    $customer2_user_name = 'customer_2_user_name' . substr(md5(mt_rand()), 0, 8);
    $customer2_admin_name = 'customer_2_admin_name' . substr(md5(mt_rand()), 0, 8);

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
        'test email validation ',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [
                                                                    'login' => $customer_user_name,
                                                                    'password' => $password1,
                                                                    ]],
        [
          ],
        [],
        [ 'key' => 'email', 'message' => 'Email required', ],
        ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer-id}', null, [
                                                                    'login' => $customer_user_name,
                                                                    'password' => $password1,
                                                                    'email' => self::generateEmail(),
                                                                    'firstname' => 'Joe',
                                                                    'lastname' => 'Customer'
                                                                    ]],
        [ 'customer-user-login' => 'login',
          'customer-staff-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       [
        'Reset this customer staff user\'s password as admin',
        [ 'POST', '/api/user/resetPassword/${customer-staff-id}', ],
        [ 'ok' => '0', ],
        [ 'ok' => 'ok', ],
        ],

       [
        'Create a customer admin',
        [ 'POST', '/api/user/customer_admin/${customer-id}', null, [
                                                                    'login' => $customer_admin_name,
                                                                    'password' => $password2,
                                                                    'email' => self::generateEmail(),
                                                                     'firstname' => 'Sally',
                                                                     'lastname' => 'Admin'
                                                                    ]],
        [ 'customer-user-login' => 'login',
          'customer-admin-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer2_name) ],
        [ 'customer2-id' => 'id' ],
        []
        ],

       [
        'Create a customer staff',
        [ 'POST', '/api/user/customer_staff/${customer2-id}', null, [
                                                                     'login' => $customer2_user_name,
                                                                     'password' => $password3,
                                                                     'email' => self::generateEmail(),
                                                                     'firstname' => 'Mary',
                                                                     'lastname' => 'Jane'
                                                                     ]],
        [ 'customer2-user-login' => 'login',
          'customer2-staff-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       [
        'Add customer staff 2 to first customer ',
        [ 'POST', '/api/user/role/customer_staff', null, [ 'userId' => '${customer2-staff-id}', 'customerId' => '${customer-id}' ]],
        [ 'customer2-user-login' => 'login',
          'customer2-staff-id' => 'id',
          'role-id' => 'authorities.0.id',
          'customer2-staff-authorities-size' => 'authorities.@size', ],
        [ 'customer2-staff-authorities-size' => '2', ],
        ],

       [
        'Add customer staff 2 to freightcom_staff  ',
        [ 'POST', '/api/user/role/freightcom_staff', null, [ 'userId' => '${customer2-staff-id}', 'customerId' => '${customer-id}' ]],
        [ 'customer2-user-login' => 'login',
          'customer2-staff-id' => 'id',
          'role-id' => 'authorities.0.id',
          'customer2-staff-authorities-size' => 'authorities.@size',
          ],
        [ 'customer2-staff-authorities-size' => '3', ],
        ],

       [
        'Create a customer admin',
        [ 'POST', '/api/user/customer_admin/${customer2-id}', null, [
                                                                     'login' => $customer2_admin_name,
                                                                     'password' => $password4,
                                                                     'email' => self::generateEmail(),
                                                                     'firstname' => 'Barley',
                                                                     'lastname' => 'Corn'
                                                                     ]],
        [ 'customer2-user-login' => 'login',
          'customer2-admin-id' => 'id',
          'role-id' => 'authorities.0.id'
          ],
        []
        ],

       // Logout admin

       [[ 'POST', '/api/logout', [], null, null ]],

       // Login as customer admin for customer 1


       self::customer_staff_login_request($customer_admin_name, $password2),
       self::login_lookup_request('CUSTOMER_ADMIN'),
       self::$login_set_role_request,

       [ 'show customer 1 staff, confirm only expected authorities visible',
         [ 'GET', '/api/customer-staff' ],
         [ 'count' => '_embedded.user.@size',
           'count2' => '_embedded.user.0.authorities.@size',
           'count3' => '_embedded.user.1.authorities.@size',
           'count4' => '_embedded.user.2.authorities.@size',
           'staff-cust-id-1' => '_embedded.user.0.authorities.0.customerId',
           'staff-cust-id-2' => '_embedded.user.1.authorities.0.customerId',
           'staff-cust-id-3' => '_embedded.user.2.authorities.0.customerId',
           ],
         [
          'count' => 3,
          'count2' => 1,
          'count3' => 1,
          'count4' => 1,
          'staff-cust-id-1' => '${customer-id}',
          'staff-cust-id-2' => '${customer-id}',
          'staff-cust-id-3' => '${customer-id}',
          ],
         ],


       // Logout customer admin

       [[ 'POST', '/api/logout', [], null, null ]],

       // Login as customer admin for customer 2


       self::customer_staff_login_request($customer2_admin_name, $password4),
       self::login_lookup_request('CUSTOMER_ADMIN'),
       self::$login_set_role_request,

       [ 'show customer staff, confirm only expected authorities visible',
         [ 'GET', '/api/customer-staff' ],
         [ 'count' => '_embedded.user.@size',
           'count2' => '_embedded.user.0.authorities.@size',
           'count3' => '_embedded.user.1.authorities.@size',
           'staff-cust-id-1' => '_embedded.user.0.authorities.0.customerId',
           'staff-cust-id-2' => '_embedded.user.1.authorities.0.customerId',
           ],
         [
          'count' => 2,
          'count2' => 1,
          'count3' => 1,
          'staff-cust-id-1' => '${customer2-id}',
          'staff-cust-id-2' => '${customer2-id}',
          ],
         ],

       [ 'load second staff user, confirm only expected authorities visible',
         [ 'GET', '/api/user/${customer-admin-id}' ],
         [
          'count' => 'authorities.@size',
          'staff-cust-id' => 'authorities.0.customerId',
          ],
         [
          'count' => 1,
          'staff-cust-id' => '${customer2-id}',
          ],
         "Access is denied",
         ],

       [
        'Reset this customer staff user\'s password as customer admin',
        [ 'POST', '/api/user/resetPassword/${customer-staff-id}', ],
        [ 'ok' => '0', ],
        [ 'ok' => 'fail', ],
        ],

       [ 'Make sure cannot reach system log',
         [ 'GET', '/api/systemlog' ],
         [],
         [],
         "Access is denied",
         ],

       // Logout customer admin

       [[ 'POST', '/api/logout', [], null, null ]],

       // Login as admin to cleanup

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

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

       [
        'Delete the customer staff',
        [ 'DELETE', '/api/user/${customer2-staff-id}' ],
        [],
        []
        ],

       [
        'Delete the customer admin',
        [ 'DELETE', '/api/user/${customer2-admin-id}' ],
        [],
        []
        ],

       [
        'Delete the customer',
        [ 'DELETE', '/api/customer/${customer2-id}' ],
        [],
        []
        ],



       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}