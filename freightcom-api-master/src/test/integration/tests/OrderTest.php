<?php

require_once(__DIR__ . '/CommonMacros.php');


class OrderTest extends CommonMacros
{
  public static function requests()
  {
    $date = self::shipDate();

    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       // Get orders

       [ 'Get orders quick', [ 'GET', '/api/order-quick', [ 'sort' => 'id,desc'], ],
         [
          'order' => '_embedded.customerOrders.0',
          'order-id' => '_embedded.customerOrders.0.id',
          ],
         []],

       [ 'Get orders', [ 'GET', '/api/order', [ 'sort' => 'id,desc'], ],
         [
          'order' => '_embedded.customerOrders.0',
          'order-id' => '_embedded.customerOrders.0.id',
          'unbilled-charges' => '_embedded.customerOrders.0.unbilledCharges',
          'total-charge' => '_embedded.customerOrders.0.totalCharge'
          ],
         [ 'unbilled-charges' => '<=${total-charge}' ]
         ],

       [ "get delivered orders",
         [ 'GET', '/api/order', [ 'orderStatusName' => 'delivered'], ],
         [
          'delivered-orders-status' => '_embedded.customerOrders.*.orderStatus.name',
          ],
         [
          'delivered-orders-status' => function($orders) {
            return all(function($status) {
                return $status === 'DELIVERED' || preg_match('/invoi/ui', $status);
              },
              $orders);
          },]
         ],

       [ 'Orders shipped from mark',
         [ 'GET', '/api/order', [ 'sort' => 'id,desc', 'shipFrom' => 'mark' ], ],
         [
          'order-count' => '_embedded.customerOrders.@size',
          ],
         [ 'order-count' => '>0',]
         ],

       [ 'Orders shipped to toronto',
         [ 'GET', '/api/order', [ 'sort' => 'id,desc', 'shipTo' => 'toronto' ],],
         [
          'order-count' => '_embedded.customerOrders.@size',
          ],
         [ 'order-count' => '>0',]
         ],

       // Get submitted orders

       [[ 'GET', '/api/submitted-orders', [ 'sort' => 'id,desc'], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       // Get submitted orders with tracking number

       [ 'Get submitted orders with tracking number not null',
         [ 'GET', '/api/submitted-orders', [ 'trackingNumber' => '@notnull'], ],
         [
          'order' => '_embedded.customerOrders.0',
          'order-id' => '_embedded.customerOrders.0.id',
          ],
         []],

       // Get submitted orders with parameters

       [[ 'GET', '/api/submitted-orders', [
                                           'page' => 0,
                                           'size' => 10,
                                           'sort' => 'shipDate,DESC'
                                           ], ],
        [
         'found-count' => '_embedded.customerOrders.@size',
         ],
        ['found-count' => '>0', ]],

       // Get submitted orders with package type name

       [[ 'GET', '/api/submitted-orders', [
                                           'packageTypeName' => 'package',
                                           'page' => 0,
                                           'size' => 10,
                                           'sort' => 'shipDate,DESC'
                                           ], ],
        [
         'found-count' => '_embedded.customerOrders.@size',
         ],
        [ 'found-count' => '>0', ]],

       // Get draft orders

       [[ 'GET', '/api/draft-orders', [ 'sort' => 'id,desc'], ],
        [
         'order' => '_embedded.customerOrders.0',
         'order-id' => '_embedded.customerOrders.0.id',
         ],
        []],

       [ 'create an order',
         [ 'POST', '/api/order/create', null, self::getPayload(), ],
         [
          'new-order' => '',
          'new-id' => 'id',
          'id1' => 'id',
          'customer-id' => 'customer.customerId' ,
          ],
         [ 'new-id' => '@notNull',
           'customer-id' => '@notNull',
           ],
         ],

       [ 'update the order',
         [ 'PUT', '/api/order/${new-id}', null, self::updatePayload(), ],
         [ 'updated-order' => '', 'new-id' => 'id', 'id2' => 'id', ],
         [ 'new-id' => '@notNull' ],
         ],

       [ 'add a comment',
         [ 'PUT', '/api/order/${new-id}/update', null, [
                                                        'comment' => 'here here'
                                                        ]]
         ],

       [ 'look at the order',
         [ 'GET', '/api/order/${new-id}', ],
         [
          'look-at-order' => '',
          'packages' => 'packages',
          'package-0-id' => 'packages.0.id',
          'ship-to-id' => 'shipTo.id',
          ],
         [
          'packages' => '@notnull',
          ],
         ],

       [ 'download the messages',
         [ 'GET', '/api/order/${new-id}/status-messages', null, [] ]
         ],

       [ 'update the packages order',
         [ 'PUT', '/api/order/${new-id}', null, function($map) {
             return self::addToOrder($map['updated-order']);
           }, ],
         [
          'updated-order2' => '',
          'id3' => 'id',
          'package-0-new-id' => 'packages.0.id',
          'package-0-desc' => 'packages.0.description',
          'package-2-desc' => 'packages.2.description',
          'ship-to-country' => 'shipTo.country',
          'ship-to-new-id' => 'shipTo.id',
          'status-name' => 'statusName',
          'status-id' => 'statusId',
          ],
         [
          'id3' => '@notNull',
          'id3' => '${id2}',
          'package-0-desc' => 'changed description',
          'package-2-desc' => 'test description',
          'package-0-new-id' => '${package-0-id}',
          'ship-to-country' => 'Canada',
          'ship-to-new-id' => '${ship-to-id}',
          'status-name' => 'DRAFT',
          'status-id' => '16',
          ],
         ],

       // search for the new order by upcoming
       [ 'search for orders tomorrow',
         [ 'GET', '/api/order', [ 'scheduledShipDate' => 'tomorrow' ] ],
         [
          'order-count' => '_embedded.customerOrders.*',
          ],
         [
          'order-count' => '>0',
          ],
         ],

       [ 'get accessorial services for a package type',
         [ 'GET', '/api/accessorialservices/search/findByType', [ 'type' => 'package', 'sort' => 'name' ]]
         ],

       [ 'select a service',
         [ 'GET', '/api/service', [ 'size' => 2 ]],
         [ 'service-id' => '_embedded.services.1.id' ],
         [ 'service-id' => '@notnull' ],
         ],

       [ 'select a quote',
         [ 'PUT', '/api/order/test-rate/${service-id}/${new-id}', null, [ 'base' => 200 ]],
         [],
         [],
         ],

       [ 'book the order',
         [ 'POST', '/api/order/book/${new-id}' ],
         [
          'charges' => 'charges',
          'markups' => 'charges.*.markup',
          'commissions' => 'charges.*.commission'
          ],
         [
          'markups' => '@notnull',
          'commissions' => '@notnull',
          'charges' => function($charges) {
             return some(function($charge) {
                 return $charge['description'] == 'Saturday Pickup'; },
               $charges);
           }
           ],
         ],

       [ 'schedule a pickup for the order',
         [ 'PUT', '/api/order/schedule-pickup/${new-id}',
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

       // Get submitted orders with parameters

       [[ 'GET', '/api/submitted-orders', [ 'scheduledShipDate' => 'custom',
                                            'scheduledShipDateFrom' => $date,
                                            'scheduledShipDateTo' => $date,
                                            'page' => 0,
                                            'size' => 10,
                                            'sort' => 'shipDate,DESC'
                                            ], ],
        [
         'found-count' => '_embedded.customerOrders.@size',
         ],
        ['found-count' => '>0', ]],

       [ 'create an order with a pickup',
         [ 'POST', '/api/order', null,
           [
            'customerId' => '${customer-id}',
            'shipTo' => [ 'city' => 'Oakville' ],
            'scheduledPickup' => [
                                  'contactName' => 'Johannes Brahms',
                                  'contactPhone' => self::randomPhone('brahms'),
                                  'contactEmail' => self::randomEmail('brahms'),
                                  'pickupDate' => self::date("+3 day"),
                                  'pickupCloseTime' => '15:30',
                                  'pickupReadyTime' => '13:15',
                                  'pickupInstructions' => 'Beware dog',
                                  'deliveryCloseTime' => '16:00',
                                  'deliveryInstructions' => 'Delivery instructions',
                                  ],
            'packageTypeName' => 'env',
            'accessorialServices' => [ 'Residential Pickup' => true ]
            ]
           ],
         [
          'order2-id' => 'id',
          'pickup-id' => 'scheduledPickup.id' ],
         [ 'pickup-id' => '@notnull' ],
         ],

       [ 'update the order',
         [ 'PUT', '/api/order/${order2-id}', null,
           [
            'scheduledPickup' => [
                                  'contactName' => 'Johannes Brahms',
                                  'contactPhone' => self::randomPhone('brahms'),
                                  'contactEmail' => self::randomEmail('brahms'),
                                  'pickupDate' => self::date("+3 day"),
                                  'pickupCloseTime' => '15:30',
                                  'pickupReadyTime' => '13:15',
                                  'pickupInstructions' => 'Beware dog',
                                  'deliveryCloseTime' => '16:00',
                                  'deliveryInstructions' => 'Delivery instructions',
                                  ],
            'accessorialServices' => [ 'Residential Pickup' => true ]
            ]
           ],
         [
          'order2-id-cpmp' => 'id',
          'pickup-id' => 'scheduledPickup.id',
          'accessorial-services' => 'accessorialServices'
          ],
         [ 'pickup-id' => '@notnull' ],
         ],

       [ 'schedule a pickup on first order',
         [ 'PUT', '/api/order/${new-id}', null,
           [
            'scheduledPickup' => [
                                  'contactName' => 'Johannes Brahms',
                                  'contactPhone' => self::randomPhone('brahms1'),
                                  'contactEmail' => self::randomEmail('brahms1'),
                                  'pickupDate' => self::date("+3 day"),
                                  'pickupCloseTime' => '15:30',
                                  'pickupReadyTime' => '13:15',
                                  'pickupInstructions' => 'Beware dog',
                                  'deliveryCloseTime' => '16:00',
                                  'deliveryInstructions' => 'Delivery instructions',
                                  ],
            ]
           ],
         [
          'pickup-2' => 'scheduledPickup',
          'pickup-id' => 'scheduledPickup.id',
          'pickup-contactName-0' => 'scheduledPickup.contactName',
          'pickup-contactEmail-0' => 'scheduledPickup.contactEmail' ],
         [
          'pickup-2' => [
                         'contactName' => 'Johannes Brahms',
                         'contactPhone' => self::randomPhone('brahms1'),
                         'contactEmail' => self::randomEmail('brahms1'),
                         'pickupDate' => self::date("+3 day"),
                         'pickupCloseTime' => '15:30',
                         'pickupReadyTime' => '13:15',
                         'pickupInstructions' => 'Beware dog',
                         'deliveryCloseTime' => '16:00',
                         'deliveryInstructions' => 'Delivery instructions',
                         ],
          'pickup-id' => '@notnull',
          'pickup-contactName-0' => 'Johannes Brahms',
          'pickup-contactEmail-0' => self::randomEmail('brahms1'),
          ],
         ],

       [ 'update the pickup',
         [ 'PUT', '/api/order/${new-id}', null,
           [
            'scheduledPickup' => [
                                  'id' => '${pickup-id}',
                                  'pickupReadyTime' => '09:00',
                                  'pickupCloseTime' => '13:01',
                                  ],
            ]
           ],
         [ 'pickup-readyTime' => 'scheduledPickup.pickupReadyTime' ],
         [ 'pickup-readyTime' => '09:00' ],
         ],

       [ 'change the pickup with no id?',
         [ 'PUT', '/api/order/${new-id}', null,
           [
            'scheduledPickup' => [
                                  'pickupReadyTime' => '09:01',
                                  'pickupCloseTime' => '13:02',
                                  ],
            ]
           ],
         [ 'pickup-readyTime-2' => 'scheduledPickup.pickupReadyTime' ],
         [ 'pickup-readyTime-2' => '09:01' ],
         ],

       [ 'set order status to 999',
         [ 'PUT', '/api/order/delete/${new-id}', null, null, ],
         [ 'ok' => '0' ],
         [ 'ok' => 'ok' ],
         ],
       ];
  }

  public static function cleanup()
  {
    return
      [

       [ 'delete the new order',
         [ 'DELETE', '/api/order/${new-id}', null, null, ],
         [ 'ok' => '0' ],
         [ 'ok' => 'ok' ],
         ],

       [ 'delete the second new order',
         [ 'DELETE', '/api/order/${order2-id}', null, null, ],
         [ 'ok' => '0' ],
         [ 'ok' => 'ok' ],
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }

  // Test adding a package and changing ship to
  public static function addToOrder($order)
  {
    $newOrder = $order;
    $packages = $order['packages'];
    $newPackage = $order['packages'][0];

    $newPackage['id'] = null;
    $newPackage['description'] = 'test description';
    $packages[] = $newPackage;

    // Let's change package 0 too
    $packages[0]['description'] = 'changed description';
    $packages[0]['weight'] = '3.5';

    $newOrder['packages'] = $packages;

    // Also change ship to property
    $shipTo = $order['shipTo'];
    $shipTo['country'] = 'Canada';

    // May not change status name
    unset($newOrder['statusName']);
    unset($newOrder['statusId']);

    // May not change charges
    unset($newOrder['charges']);

    $newOrder["accessorialServices"] = [
        "Saturday Pickup" => true,
        "Residential Delivery" => true,
        "Saturday Delivery" => true,
        "Hold For Pickup" => true,
        "Residential Pickup" => true
    ];

    $newOrder['shipTo'] = $shipTo;

    return $newOrder;
  }

  public static function updatePayload()
  {
    return [
            'totalCharge' => 32.95,
            'bolId' => '94343',
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
    },
    "customerId": 6539
}
EOF;
  return json_decode($text, true);

  }

  static function getPayload2()
  {
    return [
            'shipTo' => [ 'city' => 'Toronto' ],
            'packageTypeId' => 1,
            'packageTypeName' => 'env',
            'customerId' => '${customer-id}'
            ];
  }
}