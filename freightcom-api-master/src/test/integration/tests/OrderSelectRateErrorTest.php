<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderSelectRateErrorTest extends CommonMacros
{
  public static function requests()
  {
    $date = self::shipDate();
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

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'order-id' => 'id'
          ],
         [ 'order-id' => '@notNull'
           ],
         ],

       [ 'carrier rates',
         [ 'GET', '/api/carrier_rates/${order-id}' ],
         [ 'rate-id' => '_embedded.orderRateQuote.0.id' ],
         [ 'rate-id' => '@notnull' ],
         ],

       [ 'select the rate',
         [ 'POST', '/api/order/select-rate/${order-id}/${rate-id}', null, '{ "packages": [{ "length": 44, "description": "updated package" }] }', ],
         [],
         [],
         [
          'key' => 'packages',
          'message' => 'Update forbidden'
          ]
         ],

       [ 'select the rate',
         [ 'POST', '/api/order/select-rate/${order-id}/${rate-id}', ],
         [],
         [],
         ],

       [ 'book the order',
         [ 'POST', '/api/order/book/${order-id}' ],
         [ 'charges' => 'charges', ],
         [ 'charges' => function($charges) {
             return some(function($charge) {
                 return $charge['description'] == 'Saturday Delivery'; },
               $charges);
           }
           ],
         ],

       [ 'schedule a pickup for the order',
         [ 'PUT', '/api/order/schedule-pickup/${order-id}',
           null,
           [
            'pickupInstructions' => 'Pickp instructions',
            'dropoffInstructions' => 'Dropoff instructions',
            'pickupDate' => $date,
            'dropoffDate' => $date,
            ],
           ],
         [],
         [],
         ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }

  public static function cleanup()
  {
    return
      [

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],

       [ 'GROUP', self::adminLogin() ],

       [ 'delete the order',
         [ 'DELETE', '/api/order/${order-id}'],
         [  ],
         [],
         ],

       [ 'GROUP', self::cleanupCustomerStaff() ],

       [ 'Logout', [ 'POST', '/api/logout', [], null, null ]],

       ];
  }


  public static function shipDate()
  {
    $date = new DateTime();
    $date->setTimeZone(new DateTimeZone('UTC'));
    $date->modify('+1 day');

    return $date->format('Y-m-d');
  }

  public static function getPayload()
  {
    $date = self::shipDate();

    $text = <<<EOF
{
    "shipFrom": {
        "id": 88374,
        "country": "CA",
        "city": "Hamilton",
        "consigneeName": "DAN LAURIE INSURANCE BROKER",
        "createdAt": 1287515447000,
        "defaultTo": 0,
        "notify": 0,
        "contactEmail": "test@test.co",
        "province": "ON",
        "updatedAt": null,
        "countryName": "CANADA",
        "defaultFrom": 0,
        "consigneeId": "88374",
        "contactName": "",
        "distributionListName": null,
        "address2": "",
        "address1": "My address",
        "addressId": null,
        "deletedAt": null,
        "taxId": null,
        "residential": 0,
        "phone": "8888888888",
        "instruction": "",
        "postalCode": "L8W1G6",
        "referenceId": "",
        "resourceId": 88374,
        "createdBy": null,
        "updatedBy": null,
        "company": "DAN LAURIE INSURANCE BROKER",
        "companyName": "DAN LAURIE INSURANCE BROKER"
    },
    "shipTo": {
        "id": 88381,
        "country": "US",
        "city": "Winchester",
        "consigneeName": "THE CHANDLER LAW GROUP",
        "createdAt": 1288209036000,
        "defaultTo": 0,
        "notify": 0,
        "contactEmail": "test@test.co",
        "province": "VA",
        "updatedAt": null,
        "countryName": "UNITED STATES",
        "defaultFrom": 0,
        "consigneeId": "88381",
        "contactName": "DENA ANDERSON",
        "distributionListName": null,
        "address2": "SUITE 104",
        "address1": "My address",
        "addressId": null,
        "deletedAt": null,
        "taxId": null,
        "residential": 0,
        "phone": "8888888888",
        "instruction": "",
        "postalCode": "22601",
        "referenceId": "",
        "resourceId": 88381,
        "createdBy": null,
        "updatedBy": null,
        "company": "THE CHANDLER LAW GROUP",
        "companyName": "THE CHANDLER LAW GROUP"
    },
    "scheduledShipDate": "${date}",
    "packageTypeName": "package",
    "packages": [
        {
            "length": "3",
            "width": "3",
            "height": "3",
            "weight": "3",
            "insurance": "3",
            "description": "333333"
        },
        {
            "length": "4",
            "width": "4",
            "height": "4",
            "weight": "4",
            "insurance": "4",
            "description": "4444444444"
        }
    ],
    "signatureRequired": "Yes",
    "accessorialServices": {
        "Residential Delivery": true,
        "Saturday Delivery": true,
        "Hold For Pickup": true,
        "Residential Pickup": true
    }
}
EOF;
  return json_decode($text, true);

  }
}