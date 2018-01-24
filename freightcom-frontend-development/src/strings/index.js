import LocalizedStrings from 'react-localization';

const strings = new LocalizedStrings({
  en: {
    order: {
      tracking: 'Tracking Number',
      referenceNumber: 'Reference Number',
      uploadDescriptionPrompt: 'Please enter a description of the document:',
      invoiceGeneratedTitle: 'Invoice Generated',
      invoiceGeneratedBody:
        '<a href="/invoices/{0}">Invoice</a> for {1} has been generated...',
    },
    claim: {
      submitComment:
        'To start the claim process, please describe the issue in your words. Our team will investigate your shipment and respond to this claim until it is settled. Please supply as much information as possible.',
      userPrompt: 'Submitted for',
    },
  },
});

export default strings;
