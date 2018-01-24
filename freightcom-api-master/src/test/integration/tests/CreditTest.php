<?php

require_once(__DIR__ . '/CommonMacros.php');

class CreditTest extends CommonMacros
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

       [ 'create a credit',
         [ 'POST', '/api/credit', null, [
                                         'amount' => 44.4,
                                         'customerId' => '${customer-id}',
                                         ],],
         [ 'credit-id' => 'id' ],
         [ 'credit-id' => '@notnull' ],
         ],


       [ 'list credits',
         [ 'GET', '/api/credit', ],
         [ 'size' => '_embedded.credit.@size' ],
         [ 'size' => '>0' ],
         ],

       [ 'list credits for this customer',
         [ 'GET', '/api/credit', [ 'customerid' => '${customer-id}' ] ],
         [ 'size' => '_embedded.credit.@size',
           'matched-customer' => '_embedded.credit.0.customerId', ],
         [ 'size' => '1',
           'matched-customer' => '${customer-id}', ],
         ],


       [ 'get the created credit',
         [ 'GET', '/api/credit/${credit-id}', ],
         [ 'fetched-id' => 'id' ],
         [ 'fetched-id' => '${credit-id}' ],
         ],


       [ 'get the total credit for the customer',
         [ 'GET', '/api/customer/total-credit/${customer-id}', ],
         [ 'fetched-amount' => 'totalCredit' ],
         [ 'fetched-amount' => '44.4' ],
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'delete the credit',
         [ 'DELETE', '/api/credit/${credit-id}',],
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
