<?php

require_once(__DIR__ . '/CommonMacros.php');

class CustomerTest extends CommonMacros
{
  public static function requests()
  {
    $unique = substr(md5(mt_rand()), 0, 10);
    $customer_name = 'customer_name' . $unique . "_test";

    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       [
        'Create a customer with an admin user (expect validation error',
        [ 'POST', '/api/customer-and-staff', null, [ 'name' => $customer_name,
                                                     'active' => "true"
                                                     ]],
        [],
        [],
        [ 'key' => "country",
          'message' => "is required" ]
        ],

       [
        'Create a customer with an admin user',
        [ 'POST', '/api/customer-and-staff',
          null,
          [
           'name' => $customer_name,
           'active' => "true",
           'login' => self::randomUser('metropolis.test'),
           'password' => 'foobarwashere',
           'passwordConfirm' => 'foobarwashere',
           'contact' => 'John H. Metropolis',
           'email' => 'jon.met@palominosys.com',
           'phone' => '416-555-0987',
           'address' => 'First',
           'city' => 'First',
           'country' => 'CA',
           'province' => 'ON',
           "invoiceTerm" => "30",
           "invoiceEmail" => "foo.bar.@palominosys.com",
           "invoiceTermWarning" => "40",
           "login" => self::randomUser("customerTest"),
           "invoiceCurrency" => "CAD",
           "shippingNMFCRequired" => false,
           "autoCharge" => "IMMEDIATELY",
           "password" => self::randomPassword('customerTest'),
           "autoInvoice" => "MONTHLY",
           "province" => "ON",
           "shippingPODRequired" => true,
           "passwordConfirm" => self::randomPassword('customerTest'),
           "phone" => "416-452-5267",
           "packagePreference" => [ "id" => 4 ],
           "contact" => "Zachary Smith",
           "pastDueAction" => "DISALLOW",
           "applicableTaxes" => [ [ "taxDefinition" => [ 'id' => 1 ] ] ],
           "shipTo" => [ 'country' => 'Canada' ],
           "shipFrom" => [ 'country' => 'USA' ]
           ]],
        [ 'customer-id' => 'id',
          'active' => 'active',
          'activated-at' => 'activatedAt',
          'preferred-package-type-name' => 'packagePreference.name',
          'ship-to-country' => 'shipTo.country',
          'ship-from-country' => 'shipFrom.country'
          ],
        [ 'customer-id' => '@notnull',
          'active' => '1',
          'activated-at' => '@notnull',
          'preferred-package-type-name' => 'pallet',
          'ship-to-country' => 'Canada',
          'ship-from-country' => 'USA'
          ]
        ],

       [ 'update some fields in customer',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [
            'invoiceEmail' => 'bar.foo@palominosys.com',
            'createdAt' => '2017-05-13'
            ]
           ],
         [
          'invoice-email' => 'invoiceEmail'
          ],
         [
          'invoice-email' => 'bar.foo@palominosys.com'
          ]
         ],


       [ 'find the admin user',
         [ 'GET', '/api/user', [ 'customerId' => '${customer-id}' ]],
         [ 'admin-user-id' => '_embedded.user.0.id' ],
         [ 'admin-user-id' => '@notnull' ],
         ],

       [
        'Create another customer',
        [ 'POST', '/api/customer', null, self::sampleCustomer($customer_name,
                                                              [
                                                               'active' => false,
                                                               ]) ],
        [ 'customer-not-active-id' => 'id',
          'active' => 'active',
          'activated-at' => 'activatedAt',
          ],
        [ 'customer-not-active-id' => '@notnull',
          'active' => '0',
          'activated-at' => '@null' ]
        ],

       [ 'legacy search',
         [ 'GET', '/api/customer/search/findByNameContainingAllIgnoringCase', [ 'name' => $unique, 'size' => 1 ],],
         [ 'search-customer-id' => '_embedded.customer.0.id',
           'search-customer-name' => '_embedded.customer.0.name',
           'customer' => '_embedded.customer.0', ],
         [ 'search-customer-id' => '@notNull',
           'search-customer-name' => $customer_name,
           ],
         ],

       [ 'name like unique',
         [ 'GET', '/api/customer', [ 'name' => $unique, 'size' => 1 ],],
         [ 'search-customer-id' => '_embedded.customer.0.id',
           'search-customer-name' => '_embedded.customer.0.name',
           'customer' => '_embedded.customer.0', ],
         [
          'search-customer-id' => '@notNull',
          'search-customer-name' => $customer_name,
          ],
         ],
       [ 'address toronto',
         [ 'GET', '/api/customer', [ 'address' => 'toronto', 'size' => 1 ],],
         [ 'search-customer-id' => '_embedded.customer.0.id',
           'search-customer-name' => '_embedded.customer.0.name',
           'customer' => '_embedded.customer.0', ],
         [ 'search-customer-id' => '@notNull' ],
         ],
       [ 'all customers',
         [ 'GET', '/api/customer', [ 'active' => 'all', 'size' => 1 ],],
         [ 'search-customer-id' => '_embedded.customer.0.id',
           'search-customer-name' => '_embedded.customer.0.name',
           'customer' => '_embedded.customer.0', ],
         [ 'search-customer-id' => '@notNull' ],
         ],
       [ 'active customers',
         [ 'GET', '/api/customer', [ 'active' => '1', 'size' => 1 ],],
         [ 'search-customer-id' => '_embedded.customer.0.id',
           'search-customer-name' => '_embedded.customer.0.name',
           'customer' => '_embedded.customer.0', ],
         [ 'search-customer-id' => '@notNull' ],
         ],
       [ 'inactive customers',
         [ 'GET', '/api/customer', [ 'active' => '0', 'size' => 1 ],],
         [ 'search-customer-id' => '_embedded.customer.0.id',
           'search-customer-name' => '_embedded.customer.0.name',
           'customer' => '_embedded.customer.0', ],
         [ 'search-customer-id' => '@notNull' ],
         ],

       [ 'check customer fields',
         [ 'GET', '/api/customer/${customer-id}' ],
         [
          'allowNewOrders' => 'allowNewOrders',
          'shippingNMFCRequired' => 'shippingNMFCRequired',
          'shippingPODRequired' => 'shippingPODRequired',
          'autoCharge' => 'autoCharge',
          'suspended' => 'suspended',
          'preferred-package-type-name' => 'packagePreference.name',
          'ship-to-country' => 'shipTo.country',
          'ship-from-country' => 'shipFrom.country'
          ],
         [
          'allowNewOrders' => [ 'allow new orders is not null', '@notnull' ],
          'shippingNMFCRequired' => [ 'shipping nmfc required is not null', '@notnull' ],
          'shippingPODRequired' => [ 'shipping pod required  is not null', '@notnull' ],
          'autoCharge' => [ 'auto charge is IMMEDIATELY', 'IMMEDIATELY' ],
          'suspended' => [ 'suspended is false', false ],
          'preferred-package-type-name' => 'pallet',
          'ship-to-country' => 'Canada',
          'ship-from-country' => 'USA'
          ]
         ],


       [ 'activate',
         [ 'PUT', '/api/customer/${customer-id}', null, [
                                                         'active' => "true",
                                                         'activatedAt' => '2017-02-02',
                                                         ] ],
         [ 'active' => 'active',
           'activated-at' => 'activatedAt' ],
         [ 'active' => '1',
           'activated-at' => '@notnull'
           ]],

       [ 'deactivate',
         [ 'PUT', '/api/customer/${customer-id}', null, [
                                                         'active' => "false",
                                                         'activatedAt' => '2017-02-02',
                                                         ] ],
         [ 'active' => 'active',
           'activated-at' => 'activatedAt' ],
         [ 'active' => '0',
           'activated-at' => '@null'
           ]],

       [ 'suspend',
         [ 'PUT', '/api/customer/${customer-id}', null, [
                                                         'suspended' => true
                                                         ] ],
         [ 'suspended-2' => 'suspended' ],
         [
          'suspended-2' => [ 'suspended is true', true ]
          ]],

       [ 'change auto invoice to bi weekly',
         [ 'PUT', '/api/customer/${customer-id}', null, [
                                                         'autoInvoice' => 'BIWEEKLY'
                                                         ] ],
         [ 'auto-invoice-2' => 'autoInvoice' ],
         [
          'auto-invoice-2' => [ 'autoInvoice is BIWEEKLY', 'BIWEEKLY' ]
          ]],

       [ 'check name',
         [ 'GET', '/api/customer/${customer-id}', ],
         [ 'fetched-name' => 'name' ],
         [ 'fetched-name' => $customer_name ],
         ],

       [ 'list services',
         [ 'GET', '/api/carrier' ],
         [ 'service-id1' => '_embedded.carrier.0.id',
           'service-id2' => '_embedded.carrier.1.id',
           'service-id3' => '_embedded.carrier.2.id',
           'service-id4' => '_embedded.carrier.3.id',
           ],
         [ 'service-id1' => '@notnull',
           'service-id2' => '@notnull' ]
         ],

       [ 'update excluded services in customer',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [ 'excludedServiceIds' => '${service-id1},${service-id2}' ]
           ],
         ],

       [ 'update excluded services in customer a second time',
         [ 'PUT', '/api/customer/${customer-id}', null,
           [ 'excludedServiceIds' => '${service-id3},${service-id2}, ${service-id4}' ]
           ],
         [ 'excludedServiceIds' => 'excludedServicesIds' ],
         [ 'excludedServiceIds' => function($excludedServiceIds, $map) {
             return all(function($serviceId) use ($map) {
                 return $serviceId == $map['service-id3']
                 || $serviceId == $map['service-id2']
                 || $serviceId == $map['service-id4'];
               }, $excludedServiceIds);
           }
           ]
         ],

       [ 'list agents',
         [ 'GET', '/api/agent' ],
         [ 'agent-id' => '_embedded.agent.0.id' ],
         [ 'agent-id' => '@notnull' ]
         ],

       [ 'update sales agent in customer',
         [ 'PUT', '/api/customer/${customer-id}', null,
           ["province" => "ON","invoiceEmail" => "bar.foo@palominosys.com","shippingPODRequired" => true,"city" => "District 99","invoiceTerm" => 30,"invoiceTermWarning" => 40,"postalCode" => "M3A 5A3","invoiceCurrency" => "CAD","salesAgent" => ["phone" => "(905) 510-4948","term" => "NEVER","allowNewOrders" => true,"parentSalesAgentId" => null,"userId" => 7455,"commissionPercent" => 43,"parentSalesAgentName" => null,"customerCount" => 4,"paidCommission" => 67.49,"unpaidCommission" => 1114.13,"viewInvoices" => true,"address" => null,"name" => "S2 Daudlin","roleName" => "AGENT","createdAt" => "2017-01-20","updatedAt" => "2017-05-14","id" => '${agent-id}' ],"autoInvoice" => "MONTHLY","creditCards" => [["active" => null,"expiryMonth" => null,"expiryYear" => null,"isDefault" => false,"name" => null,"type" => null,"number" => ""]],"autoCharge" => "IMMEDIATELY","pastDueAction" => "DISALLOW","allowNewOrders" => true,"resourceId" => '${customer-id}',"agentId" => '${agent-id}',"country" => "Canada","address" => "1 First st.","name" => "Hard Rock Software","id" => '${customer-id}']
           ],
         [
          'invoice-email' => 'invoiceEmail'
          ],
         [
          'invoice-email' => 'bar.foo@palominosys.com'
          ]
         ]
       ];
  }

  public static function cleanup()
  {
    return
      [
       [ 'delete admin user',
         [ 'DELETE', '/api/user/${admin-user-id}', ],
         [  ],
         [  ],
         ],

       [ 'delete',
         [ 'DELETE', '/api/customer/${customer-id}', ],
         [  ],
         [  ],
         ],

       [ 'delete',
         [ 'DELETE', '/api/customer/${customer-not-active-id}', ],
         [  ],
         [  ],
         ],

       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }
}