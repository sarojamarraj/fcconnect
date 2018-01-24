<?php


require_once(__DIR__ . '/CommonMacros.php');

class CarrierUploadInvoiceTest extends CommonMacros
{
  public static function requests()
  {
    // [[ verb, route, queryString, postData, formData, fileName], decode, validate ]
    return
      [
       [ 'GROUP', self::adminLogin() ],

       [ 'Find a service',
         [ 'GET', '/api/carrier', [ 'sort' => 'id,desc' ]],
         [ 'carrier-id' => '_embedded.carrier.0.id' ],
         [ 'carrier-id' => '@notnull' ]
         ],

       [ 'Upload an invoice',
         [ 'POST', '/api/carrier/${carrier-id}/upload-invoice', null, null,
           [ 'file' => '@' . __DIR__ . '/data/invoice.csv',
             'foo' => 'bar',
             'john' => 'smith' ]],
         [
          'invoice-path' => 'path',
          'invoice-id' => 'invoiceId'
          ],
         [
          'invoice-path' => '@notnull',
          'invoice-id' => '@notnull'
          ]
         ],

       [ 'list the processed invoices for this carrier',
         [ 'GET', '/api/carrier/invoice/${carrier-id}', [ 'processed' => 1 ]],
         [],
         []
         ],

       [ 'list the unprocessed invoices for this carrier',
         [ 'GET', '/api/carrier/invoice/${carrier-id}', [ 'processed' => 0 ]],
         [],
         []
         ],

       [ 'list all  invoices for this carrier',
         [ 'GET', '/api/carrier/invoice/${carrier-id}', [ 'sort' => 'id,desc' ]],
         [],
         []
         ],

       [ 'list all  invoices ',
         [ 'GET', '/api/carrier/invoice', [ 'sort' => 'id,desc' ]],
         [],
         []
         ],

       [ 'list all unprocessed  invoices ',
         [ 'GET', '/api/carrier/invoice', [ 'processed' => 'false' ]],
         [],
         []
         ],

       [ 'Download the uploaded invoice',
         [ 'GET', '/api/carrier/invoice/${carrier-id}/${invoice-id}'],
         [],
         []
         ],

       [ 'logout', [ 'POST', '/api/logout', [], null, null ]]
       ];
  }
}