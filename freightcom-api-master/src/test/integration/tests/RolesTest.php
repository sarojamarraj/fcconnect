<?php

require_once(__DIR__ . '/CommonMacros.php');

class RolesTest extends CommonMacros
{
  public static function requests()
  {
    $rand_first_name = 'first' . substr(md5(mt_rand()), 0, 8);
    $rand_last_name = 'last' . substr(md5(mt_rand()), 0, 8);
    $rand_first_admin = 'firstadmin' . substr(md5(mt_rand()), 0, 8);
    $rand_last_admin = 'lastadmin' . substr(md5(mt_rand()), 0, 8);
    $rand_first_staff = 'firststaff' . substr(md5(mt_rand()), 0, 8);
    $rand_last_staff = 'laststaff' . substr(md5(mt_rand()), 0, 8);

    $uniqueUser = "george." . substr(md5(mt_rand()), 0, 8) . '.jonas';

    return
      [
       [[ 'POST', '/api/logout', [], null, null ]],
       [[ 'POST', '/api/user/resetPasswordByEmail', null, [ 'email' => 'bryan.m.kramer@gmail.com' ]],
        [],
        [],
        ],

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [[ 'GET', '/api/session-role' ]],
       [[ 'GET', '/api/user', [], null, null ],],

       [ 'try a call to list api',
         [ 'GET', '/api/', [], null, null ],
         [ 'orderHref' => '_links.orderStatuses.href' ],
         [ 'orderHref' => '@notnull', ],
         ],

       [[ 'GET', '/api/addressbook' ]],

       [ "find a customer",
         [ 'GET', '/api/customer', [ 'size' => 1, 'page' => 3 ]],
         [ 'customer-id' => '_embedded.customer.0.id',
           'customer-name' => '_embedded.customer.0.name', ],
         [ 'customer-id' => '@notnull' ],
         ],

       [ "get customer staff for customer",
         [ 'GET', '/api/customer-staff', [ 'customer_id' => '${customer-id}' ]],
         [ 'customer-staff-id' => '_embedded.user.0.id', ],
         [ 'customer-staff-id' => '@notnull' ],
         ],

       [[ 'GET', '/api/customer-staff/${customer-staff-id}', [], null, null ],
        [ 'name' => 'authorities.0.customerName' ],
        [ 'name' => '${customer-name}' ],
        ],

       [[ 'GET', '/api/user/${user-id}', [], null, null ],
        [ 'roleName' => 'authorities.0.roleName' ],
        [ 'roleName' => 'ADMIN' ],
        ],

       ['First customer staff role',
        [ 'GET', '/api/customer-staff', [ 'customer_id' => '4112' ], null, null ],
        [ 'roleNames' => '_embedded.user.0.authorities.*.roleName' ],
        [ 'roleNames' => function($roles) {
            return is_array($roles) && (in_array('CUSTOMER_STAFF', $roles)
            || in_array('CUSTOMER_ADMIN', $roles));
          } ],
        ],

       [[ 'GET', '/api/logged-in-user', null, null, null ],
        [ 'login' => 'login' ],
        [ 'login' => 'admin' ],
        ],

       // Create a user

       [[ 'POST', '/api/user', null, [ 'login' => $uniqueUser, 'firstname' => 'George', 'lastname' => 'Jonas', 'email' => self::generateEmail(),  ], null ],
        [ 'login' => 'login', 'firstname' => 'firstname', 'lastname' => 'lastname', 'id' => 'id' ],
        [ 'login' => $uniqueUser, 'firstname' => 'George', 'lastname' => 'Jonas' ],
        ],

       // Find by login

       [[ 'GET', '/api/user/search/findByLogin', [ 'name' => $uniqueUser ], null, null ],
        [ 'login' => '_embedded.user.0.login' ],
        [ 'login' => $uniqueUser ],
        ],

       // Fetch newly created user

       [[ 'GET', '/api/user/${id}', null, null, null ],
        [ 'login' => 'login' ],
        [ 'login' => $uniqueUser ],
        ],

       // Update newly created user

       [[ 'PUT', '/api/user/${id}', null, [ 'firstname' => 'test1', 'lastname' => 'test2' ], null ],
        [ 'firstname' => 'firstname', 'lastname' => 'lastname', 'confirm-id' => 'id' ],
        [ 'firstname' => 'test1', 'lastname' => 'test2', 'confirm-id' => '${id}', ],
        ],

       // Fetch updated user to check update persisted

       [[ 'GET', '/api/user/${id}', null, null, null ],
        [ 'firstname' => 'firstname', 'lastname' => 'lastname', 'confirm-id' => 'id' ],
        [ 'firstname' => 'test1', 'lastname' => 'test2', 'confirm-id' => '${id}', ],
        ],

       // Update newly created user - alternate update route

       [[ 'PUT', '/api/user/update/${id}', null, [ 'firstname' => $rand_first_name, 'lastname' => $rand_last_name ], null ],
        [ 'firstname' => 'firstname', 'lastname' => 'lastname', 'confirm-id' => 'id' ],
        [ 'firstname' => $rand_first_name, 'lastname' => $rand_last_name, 'confirm-id' => '${id}', ],
        ],

       // Delete this user

       [[ 'DELETE', '/api/user/${id}', null, null, null ],
        [ 'ok' => '0', ],
        [ 'ok' => 'ok', ],
        ],


       // Add a new customer admin user

       [[ 'POST', '/api/user/customer_admin/4478', null, [
                                                          'firstname' => $rand_first_admin,
                                                          'lastname' => $rand_last_admin,
                                                          'email' => self::generateEmail(),
                                                          'login' => self::generateEmail(),
                                                          ], null ],
        [ 'firstname' => 'firstname', 'lastname' => 'lastname', 'admin-id' => 'id' ],
        [ 'firstname' => $rand_first_admin, 'lastname' => $rand_last_admin, ],
        ],

       // Check that it has desired role

       [[ 'GET', '/api/user/${admin-id}', null, null, null ],
        [
         'role-name' => 'authorities.0.roleName',
         'auth-id' => 'authorities.0.id' ],
        [ 'role-name' => 'CUSTOMER_ADMIN' ],
        ],

       // Add agent role

       [[ 'POST', '/api/user/role/agent', null, [ 'userId' => '${admin-id}' ], null ],
        [ 'roles' => 'authorities.*.roleName' ],
        ],

       // Add freightcom staff role

       [[ 'POST', '/api/user/role/freightcom_staff', null, [ 'userId' => '${admin-id}' ], null ],
        [  'user-roles' => 'authorities.*.roleName' ],
        [  'user-roles' => function($roles) {
            return is_array($roles) && in_array('FREIGHTCOM_STAFF', $roles);
          }],
        ],

       // Add customer staff role

       [[ 'POST', '/api/user/role/customer_staff', null, [ 'userId' => '${admin-id}', 'customerId' => '4478' ], null ],
        [  'user-roles' => 'authorities.*.roleName' ],
        [  'user-roles' => function($roles) {
            return is_array($roles) && in_array('FREIGHTCOM_STAFF', $roles);
          }],
        ],

       // Add another customer admin role

       [[ 'POST', '/api/user/role/customer_admin', null, [ 'userId' => '${admin-id}', 'customerId' => '4478' ], null ],
        [  'user-roles' => 'authorities.*.roleName' ],
        [  'user-roles' => function($roles) {
            return is_array($roles) && in_array('CUSTOMER_ADMIN', $roles);
          } ],
        ],

       // Check that it has desired roles

       [[ 'GET', '/api/user/${admin-id}', null, null, null ],
        [
         'role-names' => 'authorities.*.roleName',
         'role-names2' => 'authorities.*.roleName',
         'auth-id' => 'authorities.1.id' ],
        [
         'role-names2' => function($roles) {
           return is_array($roles) && in_array('CUSTOMER_ADMIN', $roles);
         },
         'role-names' => function($roles) {
           return is_array($roles) && in_array('FREIGHTCOM_STAFF', $roles);
         },
         ],
        ],

       // Check that it has desired roles

       [ 'Check Get Agent',
        [ 'GET', '/api/user/${admin-id}', null, null, null ],
        [ 'roles' => 'authorities.*.roleName' ],
        [ 'roles' => function($roles) {
            return is_array($roles) && in_array('AGENT', $roles);
          }],
        ],

       // Add admin role

       [ "Add admin role",
        [ 'POST', '/api/user/role/admin', null, [ 'userId' => '${admin-id}' ], null ],
        [  'user-roles' => 'authorities.*.roleName' ],
        [  'user-roles' => function($roles) {
            return is_array($roles) && in_array('ADMIN', $roles);
          }],
        ],

       // Delete the cust admin role

       [[ 'DELETE', '/api/user/role/${auth-id}', null, null, null ],
        [ 'ok' => '0', ],
        [ 'ok' => 'ok', ],
        ],

       // Delete this user

       [[ 'DELETE', '/api/user/${admin-id}', null, null, null ],
        [ 'ok' => '0', ],
        [ 'ok' => 'ok', ],
        ],


       // Add a new customer staff user

       [[ 'POST', '/api/user/customer_staff/4478', null, [
                                                          'firstname' => $rand_first_staff,
                                                          'lastname' => $rand_last_staff,
                                                          'email' => self::generateEmail(),
                                                          'login' => self::generateEmail(),
                                                          ], null ],
        [ 'firstname' => 'firstname', 'lastname' => 'lastname', 'staff-id' => 'id' ],
        [ 'firstname' => $rand_first_staff, 'lastname' => $rand_last_staff, ],
        ],

       // Check that it has desired role

       [[ 'GET', '/api/user/${staff-id}', null, null, null ],
        [ 'role-name' => 'authorities.0.roleName', 'auth-id' => 'authorities.0.id' ],
        [ 'role-name' => 'CUSTOMER_STAFF' ],
        ],

       // Delete this user

       [[ 'DELETE', '/api/user/${staff-id}', null, null, null ],
        [ 'ok' => '0', ],
        [ 'ok' => 'ok', ],
        ],

       // Delete the role (role cascade delete so expect error)

       [[ 'DELETE', '/api/user/role/${auth-id}', null, null, null ],
        [ 'ok' => '0', ],
        [ 'ok' => 'ok', ],
        'No such role',
        ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}