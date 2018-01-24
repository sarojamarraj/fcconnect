<?php

/**
 * Simple test, just list invoices once
 */

require_once(__DIR__ . '/CommonMacros.php');


class AdminListInvoicesTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       self::admin_login_request(),
       self::login_lookup_request('ADMIN'),
       self::$login_set_role_request,

       // Get invoices

       [ 'List invoices', [ 'GET', '/api/invoice', [ 'sort' => 'id,desc'], ],
         [
          'size' => '_embedded.invoice.@size',
          ],
         [
          'size' => '>0',
          ]],

       [ 'List invoices in 2016 descending created date',
         [ 'GET', '/api/invoice', [
                                   'sort' => 'createdAt,desc',
                                   'createdAtFrom' => '2016-01-01',
                                   'createdAtTo' => '2016-12-31'
                                   ], ],
         [
          'size' => '_embedded.invoice.@size',
          'created-at-high' => '_embedded.invoice.0.dateGenerated'
          ],
         [
          'size' => '>0',
          ]],

       [ 'List invoices in 2016 ascending created date',
         [ 'GET', '/api/invoice', [
                                   'sort' => 'createdAt,asc',
                                   'createdAtFrom' => '2016-01-01',
                                   'createdAtTo' => '2016-12-31'
                                   ], ],
         [
          'size' => '_embedded.invoice.@size',
          'created-at-low' => '_embedded.invoice.0.dateGenerated'
          ],
         [
          'size' => '>0',
          'created-at-high' => '>${created-at-low}'
          ]],

       [ 'List invoices tracking number 1',
         [ 'GET', '/api/invoice', [ 'trackingNumber' => '1Z5F13996885845051'], ],
         [
          'tracking-size' => '_embedded.invoice.@size',
          ],
         [
          ]],

       [ 'List invoices tracking number mulitple',
         [ 'GET', '/api/invoice', [ 'trackingNumber' => '1Z5F13996885845051,1Z5F13992083909978'], ],
         [
          'tracking-size' => '_embedded.invoice.@size',
          ],
         [
          ]],

       [ 'List invoices bol number 1',
         [ 'GET', '/api/invoice', [ 'bolid' => 'FE11152'], ],
         [
          'bol-size' => '_embedded.invoice.@size',
          ],
         [
          ]],

       [ 'List invoices bol number mulitple',
         [ 'GET', '/api/invoice', [ 'bol' => 'FE11152,SA10090499'], ],
         [
          'bol-size' => '_embedded.invoice.@size',
          ],
         [
          ]],

       [ 'List invoices both bol number, tracking number  mulitple',
         [ 'GET', '/api/invoice', [
                                   'bol' => 'FE11152,SA10090499',
                                   'trackingNumber' => '1Z5F13996885845051,1Z5F13992083909978'
                                   ],

           ],
         [
          'bol-size' => '_embedded.invoice.@size',
          ],
         [
          ]],

       [ 'List invoices order ids',
         [ 'GET', '/api/invoice', [
                                   'OrDerid' => '501332,501288'
                                   ],

           ],
         [
          'several-order-ids-size' => '_embedded.invoice.@size',
          ]
         ],

       [ 'List invoices one order id',
         [ 'GET', '/api/invoice', [
                                   'OrDerid' => '501332'
                                   ],

           ],
         [
          'one-order-id-size' => '_embedded.invoice.@size',
          'one-order-invoice-id' => '_embedded.invoice.0.id'
          ],
         [
          ]],

       [ 'list orders with invoice id', [
                                         'GET',
                                         '/api/submitted-orders',

                                         [ 'invoiceid' => '${one-order-invoice-id}' ]
                                         ]],

       [ 'List invoices reference code',
         [ 'GET', '/api/invoice', [
                                   'referencecode' => '002-7554067-0808239,test contact name',
                                   'sort' => 'id,DESC',
                                   'page' => 0,
                                   'size' => 10
                                   ],

           ],
         [
          'reference-codes-size' => '_embedded.invoice.@size',
          ]
         ],
       // Logout

       [[ 'POST', '/api/logout', [], null, null ]],
       ];
  }

}