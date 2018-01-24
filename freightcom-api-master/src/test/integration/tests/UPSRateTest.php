<?php

require_once(__DIR__ . '/CommonMacros.php');

class UPSRateTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'GROUP', self::createCustomerFull(self::randomUser('staff'), self::randomPassword('staff')) ],

       [ 'Logout of admin', [ 'POST', '/api/logout', [], null, null ]],

       // Test as customer staff

       self::customer_staff_login_request(self::randomUser('staff'), self::randomPassword('staff')),
       self::login_lookup_request('CUSTOMER_STAFF'),
       self::$login_set_role_request,

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'order-id' => 'id',
          'customer-id' => 'customer.customerId' ,
          ],
         [ 'order-id' => '@notNull',
           'customer-id' => '@notNull',
           ],
         ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       [ 'GROUP', self::adminLogin() ],

       [ 'get ups rates',
         [ 'GET', '/api/order/${order-id}/test-ups', [ 'size' => 2 ]],
         [  ],
         [  ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       [ 'GROUP', self::adminLogin() ],
       [
        'Delete the order',
        [ 'DELETE', '/api/order/${order-id}']
        ],

       [
        'Delete the customer admin',
        [ 'DELETE', '/api/user/${customer-admin-user-id}' ]
        ],

       [
        'Delete the customer staff',
        [ 'DELETE', '/api/user/${customer-staff-id}' ]
        ],

       [
        'Delete the customer',
        [ 'DELETE', '/api/customer/${customer-id}' ]
        ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       ];
  }

  static function getPayload()
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
