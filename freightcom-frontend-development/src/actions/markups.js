
export const loadCustomerMarkup = (shipmentType) => {
  fetch('/api/carrier?size=9999', { credentials: 'include' })
  .then(response => response.json())
  .then(json => {
    //console.log('customer markerup here...');
    //console.log(json);
  })
  .catch(error => {
    console.error(error);
  });

  return {
    type: 'LOAD_CUSTOMER_MARKUP'
  }
}

export const updateCustomerMarkup = () => {
  return {
    type: 'UPDATE_CUSTOMER_MARKUP',
  }
}
