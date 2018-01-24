<?php

require_once(__DIR__ . '/CommonMacros.php');

class RateTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]

    $user = self::randomUser('hex01');
    $password = self::randomPassword();

    return
      [
       [ 'GROUP', self::adminLogin() ],
       [ 'GROUP', self::createCustomerStaff($user, $password) ],

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'new-order' => '',
          'order-id' => 'id',
          'id1' => 'id',
          'customer-id' => 'customer.customerId' ,
          ],
         [ 'order-id' => '@notNull',
           'customer-id' => '@notNull',
           ],
         ],

       [ 'carrier rates',
         [ 'GET', '/api/carrier_rates/${order-id}' ],
         [
          'logo' => '_embedded.orderRateQuote.0.logo',
          'transit-days' => '_embedded.orderRateQuote.0.transitDays',
          'total-charges' => '_embedded.orderRateQuote.0.totalCharges',
          'count' => '_embedded.orderRateQuote.@size',
          ],
         [
          'count' => '>1',
          'transit-days' => '@notnull',
          'total-charges' => '@notnull',
          ],
         ],

       [
        'check logo',
        [ 'GET', '${logo}', [], null, null ],
        ],

       [ 'get more rates',
         [ 'GET', '/api/carrier_rates/more/${order-id}' ],
         [ 'count' => '_embedded.orderRateQuote.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>2' ], 'FOUND_MORE' ],

       [ 'get more rates',
         [ 'GET', '/api/carrier_rates/more/${order-id}' ],
         [ 'count' => '_embedded.orderRateQuote.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>2' ], 'FOUND_MORE' ],

       [ 'get more rates',
         [ 'GET', '/api/carrier_rates/more/${order-id}' ],
         [ 'count' => '_embedded.orderRateQuote.@size' ],
         ],

       [ 'CONDITION', [ 'count' => '>2' ], 'FOUND_MORE' ],

       [ 'get more rates',
         [ 'GET', '/api/carrier_rates/more/${order-id}' ],
         [ 'count' => '_embedded.orderRateQuote.@size' ],
         ],


       [ 'LABEL', 'FOUND_MORE' ],

       [ 'carrier rates',
         [ 'GET', '/api/carrier_rates/${order-id}' ],
         [ 'rate-id' => '_embedded.orderRateQuote.0.id' ],
         [ 'rate-id' => '@notnull' ],
         ],

       [ 'select the rate',
         [ 'POST', '/api/order/select-rate/${order-id}/${rate-id}', null,
           [
            "accessorialServices" => [
                                      "Saturday Pickup" => true,
                                      "Residential Delivery" => true,
                                      "Saturday Delivery" => true,
                                      "Hold For Pickup" => true,
                                      "Residential Pickup" => true
                                      ]
            ]
           ]
         ],

       [ 'book the order',
         [ 'POST', '/api/order/book/${order-id}' ]
         ],

       // Get some alerts until desired alert seen

       [ 'look for alert 1',
         [ 'GET', '/api/alert', [ 'objectId' => '${order-id}' ], ],
         [ 'alert-id' => '_embedded.alerts.0.id' ]
         ],

       [ 'CONDITION', [ 'alert-id' => '@notnull' ], 'FOUND_ALERTS' ],

       [ 'sleep', [ 'SLEEP', 1 ]],

       [ 'look for alert 2',
         [ 'GET', '/api/alert', [ 'objectId' => '${order-id}' ], ],
         [ 'alert-id' => '_embedded.alerts.0.id' ]
         ],

       [ 'CONDITION', [ 'alert-id' => '@notnull' ], 'FOUND_ALERTS' ],

       [ 'look for alert 3',
         [ 'GET', '/api/alert', [ 'objectId' => '${order-id}' ], ],
         [ 'alert-id' => '_embedded.alerts.0.id' ]
         ],

       [ 'CONDITION', [ 'alert-id' => '@notnull' ], 'FOUND_ALERTS' ],

       [ 'sleep', [ 'SLEEP', 1 ]],

       [ 'LABEL', 'FOUND_ALERTS' ],

       [ 'look for alert',
         [ 'GET', '/api/alert', [ 'objectId' => '${order-id}' ], ],
         [ 'alert-id' => '_embedded.alerts.0.id' ],
         [ 'alert-id' => '@notnull' ],
         ],

       [ 'delete the new order',
         [ 'DELETE', '/api/order/${order-id}', null, null, ],
         [ 'ok' => '0' ],
         [ 'ok' => 'ok' ],
         ],

       [ 'clean up orphan quotes',
         [ 'DELETE', '/api/order_rate_quote', null, null, ],
         [ 'ok' => '0' ],
         [  ],
         ],

       // Logout

       ['logout', [ 'POST', '/api/logout', [], null, null ]],

       ];
  }

  public static function shipDate()
  {
    $date = new DateTime();
    $date->setTimeZone(new DateTimeZone('UTC'));
    $date->modify('+34 day');

    return $date->format('Y-m-d');
  }

  public static function getPayload()
  {
    $shipDate = self::shipDate();

    $text = <<<EOF
{
    "scheduledShipDate": "{$shipDate}",
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
        "customerId": 6539,
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
        "customerId": 6539,
        "postalCode": "22601",
        "referenceId": "",
        "resourceId": 88381,
        "createdBy": null,
        "updatedBy": null,
        "company": "THE CHANDLER LAW GROUP",
        "companyName": "THE CHANDLER LAW GROUP"
    },
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
    "weight": "7",
    "accessorialServices": {
        "Saturday Pickup": true,
        "Residential Delivery": true,
        "Saturday Delivery": true,
        "Hold For Pickup": true,
        "Residential Pickup": true
    },
    "customerId": "\${customer-id}"
}
EOF;

   return json_decode($text, true);

  }
}