<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderPalletTest extends CommonMacros
{
  public static function requests()
  {
    $user = self::randomUser('hex01');
    $password = self::randomPassword();

    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       [ 'GROUP', self::adminLogin() ],
       [ 'GROUP', self::createCustomerStaff($user, $password) ],

       [[ 'POST', '/api/logout', [], null, null ]],


       // As customer staff, create order
       self::customer_staff_login_request($user, $password),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'create an address book entry 1',
         [ 'POST', '/api/address_book', null, [
                                               'province' => 'Ontario',
                                               'address1' => '111 Road Rd',
                                               'consigneeName' => 'Smith Inc',
                                               'city' => 'Markham' ]],
         [ 'address-book-id-1' => 'id' ],
         [],
         ],

       [ 'create an address book entry 2',
         [ 'POST', '/api/address_book', null, [
                                               'city' => 'Tolouse',
                                               'province' => 'Ohio',
                                               'consigneeName' => 'Jones Inc',
                                               'address1' => '222 Park Lane',
                                                ]],
         [ 'address-book-id-2' => 'id' ],
         [],
         ],

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'order-id' => 'id'
          ],
         [ 'order-id' => '@notNull'
           ],
         [
          'key' => 'message',
          'message' => "JSON parse error: Can not deserialize value of type java.math.BigDecimal from String 'a': not a valid representation; nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException: Can not deserialize value of type java.math.BigDecimal from String 'a': not a valid representation"
          ]
         ],


       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       [ 'GROUP', self::adminLogin() ],

       [ 'delete the entry 1',
         [ 'DELETE', '/api/address_book/${address-book-id-1}'],
         [  ],
         [],
         ],

       [ 'delete the entry 2',
         [ 'DELETE', '/api/address_book/${address-book-id-2}'],
         [  ],
         [],
         ],

       [ 'GROUP', self::cleanupCustomerStaff() ],

       [ 'Logout', [ 'POST', '/api/logout', [], null, null ]],

       ];
  }

  static function getPayload()
  {
    return '{"shipDate":"2017-03-22","shipFrom":{"country":"CA","postalCode":"m6g1a8","city":"Toronto","province":"ON","company":"","companyName":""},"shipTo":{"country":"US","postalCode":"90210","city":"Beverly Hills","province":"CA","company":"","companyName":""},"packageTypeName":"pallet","pallets":[{"length":"a","width":"a","height":"a","weight":"a","insurance":"a","nmfcCode":"a","pieces":"a","description":"a"},{"length":"a","width":"a","height":"a","weight":"a","insurance":"a","nmfcCode":"a","pieces":"a","description":"a"}],"customerId":${customer-id},"customer":null,"accessorialServices":{"Pickup Tailgate":true,"Saturday Delivery":true,"Delivery Tailgate":true,"Appointment Pickup":true}}';
  }
}