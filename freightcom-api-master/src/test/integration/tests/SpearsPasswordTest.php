<?php

require_once(__DIR__ . '/CommonMacros.php');

class SpearsPasswordTest extends CommonMacros
{
  public static function requests()
  {
    return
      [
       // Make sure spears1 has a CUST_STAFF role
       [ 'GROUP', self::adminLogin() ],

       [ 'lookup user with login spears1 and get authorities',
         [ 'GET', '/api/user', [ 'login' => 'spears1']],
         [ 'staff-role' => function($object) {
             if (isset($object['_embedded']['user'][0]['authorities'])) {
               foreach ($object['_embedded']['user'][0]['authorities'] as $authority) {
                 if ($authority['roleName'] === 'CUSTOMER_STAFF') {
                   return $authority['id'];
                 }
               }
             }

             return null;
           },
           'spears1-user-id' => '_embedded.user.0.id',
           ],
         [
          'spears1-user-id' => '@notnull',
          ],
         ],

       [ 'CONDITION', [ 'staff-role' => '@notnull' ], 'HAS-CUSTOMER_STAFF' ],

       [ 'find some random customer',
         [ 'GET', '/api/customer', [ 'size' => 1 ]],
         [ 'some-customer-id' => '_embedded.customer.0.id' ],
         [ 'some-customer-id' => '@notnull' ],
         ],

       [ "add staff role to spears1 since doesn't have",
         [ 'POST', '/api/user/role/customer_staff', null, [ 'userId' => '${spears1-user-id}',
                                                            'staffCustomerId' => '${some-customer-id}',
                                                            ]],
         [],
         [],
         ],

       [ 'LABEL', 'HAS-CUSTOMER_STAFF' ],

       [ 'Set  spears\'s password',
         [ 'PUT', '/api/user/${spears1-user-id}', null, [
                                                         'password' => getenv('FREIGHTCOM_spears1'),
                                                         'email' => 'spears1@palominosys.com',
                                                         ]],
         ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       // Actual test

       self::customer_login_request('spears1'),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'Change spears password',
         [ 'PUT', '/api/user/update/${user-id}', null, [ 'password' => 'aaaa' ]],
         ],

       [ 'Change spears password back',
         [ 'PUT', '/api/user/${user-id}', null, [ 'password' => getenv('FREIGHTCOM_spears1')  ]],
         ],

       [ 'Logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}
