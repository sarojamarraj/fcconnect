<?php

require_once(__DIR__ . '/CommonMacros.php');

class Customer2Test extends CommonMacros
{
  public static function requests()
  {
    $unique = substr(md5(mt_rand()), 0, 10);
    $customer_name = 'customer_name' . $unique . "_test";

    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Try to create a customer invalid currency',
        [ 'POST', '/api/customer', null, self::sampleCustomer(self::randomUser('c0'), [ 'invoiceCurrency' => 'foo' ]) ],
        [],
        [],
        [
         'key' => "invoiceCurrency",
         'message' => "invalid value"
         ]
        ],
       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer(self::randomUser('c0')) ],
        [ 'customer-id' => 'id',
          'active' => 'active',
          'activated-at' => 'activatedAt',
          ],
        [ 'customer-id' => '@notnull',
          'active' => false,
          'activated-at' => '@null']
        ],

       [
        'Create another customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer(self::randomUser('c2')) ],
        [ 'customer-id2' => 'id',
          'active' => 'active',
          'activated-at' => 'activatedAt',
          ],
        [ 'customer-id2' => '@notnull',
          'active' => false,
          'activated-at' => '@null']
        ],

       [
        'Try to update the first customer invalid currency',
        [ 'PUT', '/api/customer/${customer-id}', null, [ 'invoiceCurrency' => ')))' ] ],
        [],
        [],
        [
         'key' => "invoiceCurrency",
         'message' => "invalid value"
         ]
        ],

       [
        'Update the second customer',
        [ 'PUT', '/api/customer/${customer-id2}', null, self::sampleCustomer(self::randomUser('c1'),
                                                                             [ 'active' => true ]) ],
        [ 'customer-id2' => 'id',
          'active' => 'active',
          'activated-at' => 'activatedAt',
          ],
        [ 'customer-id2' => '@notnull',
          'active' => true,
          'activated-at' => '@notnull']
        ],

       [ 'delete',
         [ 'DELETE', '/api/customer/${customer-id}', ],
         [  ],
         [  ],
         ],

       [ 'delete',
         [ 'DELETE', '/api/customer/${customer-id2}', ],
         [  ],
         [  ],
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }


}