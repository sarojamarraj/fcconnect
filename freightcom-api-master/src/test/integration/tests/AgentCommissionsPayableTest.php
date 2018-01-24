<?php

require_once(__DIR__ . '/CommonMacros.php');

class AgentCommissionsPayableTest extends CommonMacros
{
  public static function requests()
  {
    return
      [

       [ 'GROUP', self::adminLogin() ],

       [ 'GROUP', self::createCustomerFull(self::randomUser('staff'), self::randomPassword('staff')) ],

       [ 'create an agent',
         [ 'POST', '/api/user/agent', null, [
                                             'login' => self::randomUser('agent'),
                                             'email' => self::randomEmail('agent'),
                                             'firstname' => 'Jacqueline',
                                             'lastname' => 'Jones',
                                             'password' => self::randomPassword('agent'),
                                             'commissionPercent' => '20'
                                             ]],
         [ 'agent-id' => 'id',
           'agent-role-id' => 'authorities.0.id' ],
         [
          [ 'agent id is not null', 'agent-id', '@notnull' ],
          [ 'agent role id is not null', 'agent-role-id', '@notnull' ]
          ],
         ],

       [ 'Assign agent to customer',
         [ 'PUT', '/api/customer/${customer-id}', null, '{ "salesAgent": { "id": "${agent-role-id}" } }' ]
         ],

       [ 'look at customer',
         [ 'GET', '/api/customer/${customer-id}' ],
         ],

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

       [ 'select a service',
         [ 'GET', '/api/service', [ 'size' => 2 ]],
         [ 'service-id' => '_embedded.services.1.id' ],
         [ 'service-id' => '@notnull' ],
         ],

       [ 'select a quote',
         [ 'PUT', '/api/order/test-rate/${service-id}/${order-id}', null, [ 'base' => 200 ]],
         [],
         [],
         ],

       [ 'book the order',
         [ 'POST', '/api/order/book/${order-id}' ],
         [ 'charge-agent-ids' => 'charges.*.agentId',
           'first-charge-id' => 'charges.0.id' ],
         [
          [ 'First charge id is not null', 'first-charge-id', '@notnull' ],
          [
           'Check that all charges have the agent id',
           'charge-agent-ids',
           function($agentIds, $map) {
             return all(function($agentId) use ($map) {
                 return $agentId == $map['agent-role-id'];
               }, $agentIds);
           }
           ]
          ],
         ],

       [ 'list orders with unbilled charges',
         [ 'GET', '/api/submitted-orders',
           [ 'invoiceStatus' => 'unbilled charges',
             'sort' => 'id,desc']]
         ],

       // [ 'ffo foo', [ 'GET', 'varf' ]],

       [ 'view the order to check unbilled charges',
         [ 'GET', '/api/order/${order-id}' ],
         [
          'unbilled-charges' => 'unbilledCharges',
          'charge-subtotals' => 'charges.*.subTotal',
          'charge-totals' => 'charges.*.total',
          'charge-statuses' => 'charges.*.status',
          'charge-currencies' => 'charges.*.currency',
          'charge-descriptions' => 'charges.*.description',
          'second-charge-id' => 'charges.1.id',
          'third-charge-id' => 'charges.2.id',
          'charges' => 'charges'
          ],
         [
          [ 'Unbilled charges > 0', 'unbilled-charges', '>0' ],
          [ 'Charge total = unbilled charges', 'charge-subtotals', function($totals, $map) {
              return reduce(function($chargeSubtotal, $total) {
                  return $total + $chargeSubtotal;
                }, $totals, 0) == $map['unbilled-charges'];
            }],
          [ 'No null charge status', 'charge-statuses', function($statuses) {
              return all(function($status) { return $status !== null; }, $statuses);
            }],
          [ 'No null currencies', 'charge-currencies', function($currencies) {
              return all(function($currency) { return $currency !== null; }, $currencies);
            }],
          [ 'No null description', 'charge-descriptions', function($descriptions) {
              return all(function($description) { return $description !== null; }, $descriptions);
            }],
          ['Charge total = sub total + totalTax', 'charges', function($charges) {
              return all(function($charge) {
                  return $charge['total'] == $charge['subTotal'] + $charge['totalTax'];
                }, $charges);
            }],
          ['Charge subtotal = charge * quantity', 'charges', function($charges) {
              return all(function($charge) {
                  return $charge['subTotal'] == $charge['charge'] * $charge['quantity'];
                }, $charges);
            }]
          ],
         ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
       [ 'GROUP', self::adminLogin() ],

       [ 'add a charge',
         [ 'POST', '/api/order/${order-id}/charge', null, [ 'charge' => 200,
                                                            'cost' => '163',
                                                            'accessorialId' => 15 ]],
         [
          'new-charge-cost' => 'cost',
          'new-charge-commission' => 'applyCommission'
          ],
         [
          'new-charge-cost' => 163,
          'new-charge-commission' => true
          ]
         ],

       [ 'add a charge no commission',
         [ 'POST', '/api/order/${order-id}/charge', null, [ 'charge' => 250,
                                                            'cost' => '263',
                                                            'applyCommission' => false,
                                                            'accessorialId' => 15 ]],
         [
          'new-charge-2-cost' => 'cost',
          'new-charge-2-commission' => 'applyCommission'
          ],
         [
          'new-charge-2-cost' => 263,
          'new-charge-2-commission' => false
           ]
         ],

       [ 'find a carrier',
         [ 'GET', '/api/carrier', [ 'sort' => 'id,desc', 'size' => 1]],
         [ 'carrier-id' => '_embedded.carrier.0.id' ],
         [ 'carrier-id' => '@notnull' ]
         ],

       [ 'change a charge',
         [ 'PUT', '/api/order/charge/${third-charge-id}', null,
           [
            'accessorialId' => -1,
            'description' => 'somebody needs breakfast',
            'carrierId' => '${carrier-id}',
            'quantity' => 1,
            'cost' => '343',
            'charge' => 400 ]],
         [ 'changed-charge-cost' => 'cost' ],
         [ 'changed-charge-cost' => 343 ],
         ],

       [ 'remove a charge',
         [ 'DELETE', '/api/order/charge/${second-charge-id}'],
         [],
         [],
         ],

       [ 'Reconcile a charge',
         [ 'PUT', '/api/charge/mark-reconciled/${first-charge-id}' ],
         [ 'reconciled' => 'reconciled' ],
         [ [ 'Charge is reconciled', 'reconciled', true ] ]
         ],

       [ 'view the order to check charges',
         [ 'GET', '/api/order/${order-id}' ],
         [ 'charges' => 'charges'],
         [
          [ 'check that first charge id is reconciled', 'charges', function($charges, $map) {
              return find(function($charge) use ($map) { return $charge['id'] == $map['first-charge-id']; }, $charges)['reconciled'];
            }],
          [ 'check that second charge id is not there', 'charges', function($charges, $map) {
              return ! some(function($charge) use ($map) { return $charge['id'] == $map['second-charge-id']; }, $charges);
            }],
          [ 'check that new charge is there', 'charges', function($charges, $map) {
              return some(function($charge) use ($map) { return $charge['cost'] == 163; }, $charges);
            } ],
          [ 'check that the changed charge is chagned', 'charges', function($charges, $map) {
              return some(function($charge) use ($map) { return $charge['id'] == $map['third-charge-id'] && $charge['description'] == 'somebody needs breakfast'; }, $charges);
            } ]
          ]
         ],

       [ 'show the status messages after editing, updating charges',
         [ 'GET', '/api/order/${order-id}/status-messages' ],
         [ 'events' => '_embedded.loggedEvents' ],
         [ 'events' => function($events) {
                                          return some(function($event) {
                                              return $event['message'] === 'Charge deleted';
                                            }, $events);
           }],
         ],

       [ 'create an invoice',
         [ 'POST', '/api/generate-invoice', null, [
                                                   'orders' => [ [ 'id' => '${order-id}',]],
                                                   'customer' => [ 'id' => '${customer-id}' ],
                                                   ],
           ],
         [ 'invoice-id' => 'id' ],
         [ 'invoice-id' => '@notnull' ],
         ],

       [ 'Look at the commissions values for the agent',
         [ 'GET', '/api/agent/${agent-role-id}' ]
         ],

       [ 'Look at the commissions values for the agent using list search before report?',
         [ 'GET', '/api/agent', [ 'id' => '${agent-role-id}' ] ]
         ],

       [ 'Reconcile all charges in order',
         [ 'PUT', '/api/order/mark-reconciled/${order-id}' ],
         [ 'reconciled-2' => 'charges.1.reconciled' ],
         [ [ 'Charge 2 is reconciled', 'reconciled-2', true ] ]
         ],

       [ 'generate commissions',
         [ 'PUT', '/api/calculate-commissions' ],
         [
          'commission-statement-ids' => '*.id',
           'commission-statement-id-0' => '0.id'
           ],
         [ 'commission-statement-ids' => '@notnull' ]
         ],

       [ 'List commissions reports',
         [ 'GET', '/api/commissionstatement', [ 'agentid' => '${agent-role-id}' ] ],
         [ 'commission-statement-id' => '_embedded.commissionPayable.0.id' ],
         [
          [ 'listed id is same as previously created', 'commission-statement-id',
            function($id, $map) {
              return some(function($listId) use($id) {
                  return $id == $listId;
                }, $map['commission-statement-ids']);
            } ]
          ]
         ],

       [ 'Mark one paid',
         [ 'PUT', '/api/commissionstatement/${commission-statement-id-0}/paid' ],
         [ 'pay-id' => 'id' ],
         [ 'pay-id' => '${commission-statement-id-0}' ]
         ],

       [ 'List all commissions reports',
         [ 'GET', '/api/commissionstatement', [ 'agentid' => '${agent-role-id}' ] ]
         ],

       [ 'Show single commission statement',
         [ 'GET', '/api/commissionstatement/${commission-statement-id}' ]
         ],

       [ 'Look at the commissions values for the agent after report?',
         [ 'GET', '/api/agent/${agent-role-id}' ]
         ],

       [ 'Look at the commissions values for the agent using list search after report?',
         [ 'GET', '/api/agent', [ 'id' => '${agent-role-id}' ]]
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
        'Delete the commission statement',
        [ 'DELETE', '/api/commissionstatement/${commission-statement-id}']
        ],

       [
        'Delete the invoice',
        [ 'DELETE', '/api/invoice/${invoice-id}']
        ],

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

       // Don't delete the agent for now, messes up system
       // [
       //  'Delete the agent',
       //  [ 'DELETE', '/api/user/${agent-id}' ]
       //  ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]],
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
            'shipDate' => self::date('+4 weeks'),
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
