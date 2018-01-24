<?php

require_once(__DIR__ . '/CommonMacros.php');

class SdaudlinInvoiceTest extends CommonMacros
{
  public static function requests()
  {
    return
      [
       [ 'GROUP', self::adminLogin() ],

       [ 'lookup user with login sdaudlin and get authorities',
         [ 'GET', '/api/user', [ 'login' => 'sdaudlin']],
         [ 'admin-role' => function($object) {
             if (isset($object['_embedded']['user'][0]['authorities'])) {
               foreach ($object['_embedded']['user'][0]['authorities'] as $authority) {
                 if ($authority['roleName'] === 'ADMIN') {
                   return $authority['id'];
                 }
               }
             }

             return null;
           },
           'sdaudlin-user-id' => '_embedded.user.0.id',
           ],
         [
          'sdaudlin-user-id' => '@notnull',
          ],
         ],

       [ 'CONDITION', [ 'admin-role' => '@notnull' ], 'HAS-ADMIN' ],

       [ "add admin role to sdaudlin since doesn't have",
         [ 'POST', '/api/user/role/admin', null, [ 'userId' => '${sdaudlin-user-id}' ]],
         [],
         [],
         ],

       [ 'LABEL', 'HAS-ADMIN' ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       self::customer_login_request('sdaudlin'),
       self::login_lookup_request('ADMIN',
                                  [ 'customer-admin-role' => 'CUSTOMER_ADMIN']),
       self::login_set_role_request('login-role'),

       [ 'get all invoices as admin',
         [ 'GET', '/api/invoice' ],
         [ 'size' => '_embedded.invoice.@size' ],
         [ 'size' => '>0' ],
         ],

       [ 'get some submitted orders as admin',
         [ 'GET', '/api/submitted-orders', [
                                            'page' => 0,
                                            'size' => 10,
                                            'sort' => 'id,desc',
                                            ] ],
         [ 'size' => '_embedded.order.@size' ],
         [  ],
         ],

       [ 'get some submitted orders for agentid as admin',
         [ 'GET', '/api/submitted-orders', [
                                            'page' => 0,
                                            'size' => 10,
                                            'customerId' => 8051,
                                            'sort' => 'id,desc',
                                            ] ],
         [ 'size' => '_embedded.order.@size' ],
         [  ],
         ],

       self::login_set_role_request('customer-admin-role'),

       [ 'get all invoices as customer admin',
         [ 'GET', '/api/invoice', [ 'size' => 10 ] ],
         [ 'size' => '_embedded.invoice.@size' ],
         [  ],
         ],

       [ 'get some submitted orders as customer admin',
         [ 'GET', '/api/submitted-orders', [
                                            'page' => 0,
                                            'size' => 10,
                                            'sort' => 'id,desc',
                                            ] ],
         [ 'size' => '_embedded.order.@size' ],
         [  ],
         ],

       [ 'get some orders as customer admin',
         [ 'GET', '/api/order', [
                                 'page' => 0,
                                 'size' => 10,
                                 'sort' => 'id,desc',
                                 ] ],
         [ 'size' => '_embedded.order.@size' ],
         [  ],
         ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }

}
