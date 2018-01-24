<?php

require_once(__DIR__ . '/CommonMacros.php');

class CurrencyExchangeTest extends CommonMacros
{
  public static function requests()
  {
    $password = substr(md5(mt_rand()), 0, 18);
    $customer_staff_name = 'cust.staff' . substr(md5(mt_rand()), 0, 18);


    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'list exchange rates',
         [ 'GET', '/api/currency-exchange' ],
         [ 'size' => '_embedded.currencyExchange.@size',
           'id-1' => '_embedded.currencyExchange.0.id',
           'rate-1' => '_embedded.currencyExchange.0.rate',
           'updated-1' => '_embedded.currencyExchange.0.updatedAt',
           ],
         [ 'size' => '>1',
           'id-1' => '@notnull',
           'rate-1' => '@notnull', ],
         ],

       [ 'update',
         [ 'PUT', '/api/currency-exchange/${id-1}', null,
           [ 'rate' => function($map) {
               return $map['rate-1'] + .01;
             }]]
         ],

       [ 'GROUP', self::createCustomerStaff($customer_staff_name, $password) ],
       [[ 'POST', '/api/logout', [], null, null ]],

       // Login as customer staff
       self::customer_staff_login_request($customer_staff_name, $password),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'list exchange rates',
         [ 'GET', '/api/currency-exchange' ],
         [ 'size' => '_embedded.currencyExchange.@size',
           'id-1' => '_embedded.currencyExchange.0.id',
           'rate-1' => '_embedded.currencyExchange.0.rate',
           'updated-1' => '_embedded.currencyExchange.0.updatedAt',
           ],
         [ 'size' => '>1',
           'id-1' => '@notnull',
           'rate-1' => '@notnull', ],
         ],

       [ 'update should fail as customer staff',
         [ 'PUT', '/api/currency-exchange/${id-1}', null,
           [ 'rate' => function($map) {
               return $map['rate-1'] + .01;
             }]],
         [],
         [],
         "Access is denied",
         ],

       [[ 'POST', '/api/logout', [], null, null ]],

       // Clean up
       [ 'GROUP', self::adminLogin() ],
       [ 'GROUP', self::cleanupCustomerStaff() ],
       [[ 'POST', '/api/logout', [], null, null ]],


       ];
  }
}
