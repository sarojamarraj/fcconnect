<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderAddressTest extends CommonMacros
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
         ],


       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }

  public static function cleanup()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'delete the order',
         [ 'DELETE', '/api/order/${order-id}'],
         [  ],
         [],
         ],

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
    return [
            'shipTo' => [ 'city' => 'Toronto' ],
            'packageTypeId' => 1,
            'packageTypeName' => 'env',
            'customerId' => '${customer-id}',
            "signatureRequired" => "Yes",
            "accessorialServices" => [
                                      "Saturday Pickup" => true,
                                      "Residential Delivery" => true,
                                      "Saturday Delivery" => true,
                                      "Hold For Pickup" => true,
                                      "Residential Pickup" => true
                                      ],
            ];
  }
}