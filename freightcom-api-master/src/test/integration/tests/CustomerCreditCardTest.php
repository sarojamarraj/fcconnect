<?php

require_once(__DIR__ . '/CommonMacros.php');

class CustomerCreditCardTest extends CommonMacros
{
  public static function requests()
  {
    $customer_name = 'customer_name' . substr(md5(mt_rand()), 0, 8);
    $cc_number1 = '4242424242';
    $cc_number2 = '35353535353';
    $cc_number1_match = '******4242';
    $cc_number2_match = '*******5353';

    return
      [

       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,
       [
        'Create a customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name,
                                                              [
                                                               'creditCards' => [ [ 'number' => $cc_number1 ] ],
                                                               ]) ],
        [
         'customer-id' => 'id',
         'credit-card' => 'creditCards.0.number' ]
        ],

       [ 'Show the customer',
         [ 'GET', '/api/customer/${customer-id}' ],
         [ 'credit-card' => 'creditCards.0.number' ]
         ],

       [ 'update the card number and cvc and make default',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [
            'creditCards' => [ [ 'number' => $cc_number2, 'cvc' => '343', 'isDefault' => true ] ],
            ],
           ],
         [ 'credit-card' => 'creditCards.0.number',
           'cvc' => 'creditCards.0.cvc',
           'is-default' => 'creditCards.0.isDefault',
           ]
         ],

       [ 'Show the customer 2',
         [ 'GET', '/api/customer/${customer-id}' ],
         [ 'credit-card' => 'creditCards.0.number' ]
         ],

       [ 'add a card validation error',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [
            'creditCards' => [ [ 'number' => $cc_number2, 'cvc' => '343', 'isDefault' => true ],
                               [ 'number' => $cc_number1, 'cvc' => '311', 'isDefault' => true ]
                               ],
            ],
           ],
         [ 'credit-card' => 'creditCards.0.number',
           'cvc' => 'creditCards.0.cvc',
           'is-default' => 'creditCards.0.isDefault',
           ],
         [ 'credit-card' => $cc_number2_match,
           'cvc' => '343',
           'is-default' => true
           ],
         [
          "key" => "creditCards",
          "message" => "More than one default" ]
         ],

       [ 'add a card',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [
            'creditCards' => [ [ 'number' => $cc_number2, 'cvc' => '343', 'isDefault' => true ],
                               [ 'number' => $cc_number1, 'cvc' => '311', 'isDefault' => false ],
                               [ "name" => "Tester1",
                                 "type" => "visa",
                                 "cvc" => "487",
                                 'number' => '34343311111',
                                 "expiryMonth" => "06",
                                 "expiryYear" => "17"
                                 ]
                               ],
            ],
           ],
         [ 'credit-card' => 'creditCards.0.number',
           'cvc' => 'creditCards.0.cvc',
           'is-default' => 'creditCards.0.isDefault',
           ]
         ],

       [ 'Show the customer with added cards',
         [ 'GET', '/api/customer/${customer-id}' ],
         [ 'credit-card' => 'creditCards.0.number' ]
         ],

       [ 'List some customers to check that cc is not visible',
         [ 'GET', '/api/customer', [ 'id' => '${customer-id}' ]],
         [ 'credit-card' => '_embedded.customer.0.creditCards.0.number' ],
         [ 'credit-card' => '@null' ]
         ],

       [ 'remove the card',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [
            'creditCards' => null,
            ],
           ],
         [ 'credit-card' => 'creditCard.number' ],
         [ 'credit-card' => '@null' ]
         ],

       [ 'Show the customer 2',
         [ 'GET', '/api/customer/${customer-id}' ],
         [ 'credit-card' => 'creditCards.0.number' ],
         [ 'credit-card' => '@null' ]
         ]
       ];
    }

  public static function cleanup()
  {
    return
      [
       [ 'Delete the customer',
         [ 'DELETE', '/api/customer/${customer-id}' ],
         ],


       [ "Logout", [ 'POST', '/api/logout', [], null, null ]],
       ];
  }


}
