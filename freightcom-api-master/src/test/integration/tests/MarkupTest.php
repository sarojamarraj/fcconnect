<?php

require_once(__DIR__ . '/CommonMacros.php');

class MarkupTest extends CommonMacros
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
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name) ],
        [ 'customer-id' => 'id',
          ],
        [
         'customer-id' => '@notnull',
         ]
        ],

       [ 'create a markup',
         [ 'POST', '/api/markup', null, [
                                         'minAmount' => 33.3,
                                         'maxAmount' => 44.4,
                                         'customerId' => '${customer-id}',
                                         ],],
         [ 'markup-id' => 'id' ],
         [ 'markup-id' => '@notnull' ],
         ],


       [ 'list markups',
         [ 'GET', '/api/markup', ],
         [ 'size' => '_embedded.markup.@size' ],
         [ 'size' => '>0' ],
         ],


       [ 'list markups for this customer',
         [ 'GET', '/api/markup', [ 'customerid' => '${customer-id}' ]],
         [ 'size' => '_embedded.markup.@size',
           'matched-customer' => '_embedded.markup.0.customerId', ],
         [ 'size' => '1',
           'matched-customer' => '${customer-id}', ],
         ],


       [ 'get the created markup',
         [ 'GET', '/api/markup/${markup-id}', ],
         [ 'fetched-id' => 'id' ],
         [ 'fetched-id' => '${markup-id}' ],
         ],


       [ 'delete the markup',
         [ 'DELETE', '/api/markup/${markup-id}',],
         [ 'status' => '0' ],
         [ 'status' => 'ok' ],
         ],

       [ 'delete the customer',
         [ 'DELETE', '/api/customer/${customer-id}', ],
         [  ],
         [  ],
         ],


       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],


       ];
  }
}
