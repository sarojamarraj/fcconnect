import React from 'react';
import { connect } from 'react-redux';
import { change, Field, FormSection, formValueSelector } from 'redux-form';
import { Row, Col } from 'react-bootstrap';

import InputText from '../forms/inputs/InputText';
import InputSelect from '../forms/inputs/InputSelect';
import AddressBookAutoSuggest from '../address_book/AddressBookAutoSuggest';

import { formatAddress, states, provinces, countries } from '../../utils';
import { uploadDistributionListFile } from '../../actions/orders';
import { selectActiveTab } from '../../reducers';
import { selectDistributionList } from '../../reducers';
import { formatPhone } from '../../utils';

class ShipmentAddress extends FormSection {
  static defaultProps = { showDistributionListOption: false };

  // NOTE: context is needed when extending FormSection
  constructor(props, context) {
    super(props, context);

    this.state = {
      isNewAddress: !props.hasAddressBookData,
      isInstant: false,
      selectedAddress: {},
      isDistributionList: false,
      distributionListStatus: 'Waiting',
    };

    this.autoSuggest = {};
  }

  componentWillReceiveProps(nextProps) {
    // This part checks if the form is previously an instant rate quote and
    // got changed into a full shipment order.
    // Since there's already user input for addresses, this makes
    // the new address the selected option.
    if (this.props.isInstantRate !== nextProps.isInstantRate) {
      this.setState({ isNewAddress: true });
    } else if (nextProps.hasAddressBookData && !this.props.hasAddressBookData) {
      this.setState({ isNewAddress: false });
    } else if (!nextProps.hasAddressBookData && this.props.hasAddressBookData) {
      this.setState({ isNewAddress: true });
    }

    if (Boolean(this.props.selectedAddress)) {
      if (this.props.selectedAddress.addressBookId) {
        this.setState({ selectedAddress: this.props.selectedAddress });
      } else {
        this.setState({ isNewAddress: true });
      }
    }

    if (this.props.distributionList && this.props.distributionList.id) {
      this.setState({ isDistributionList: true });
    }
  }

  componentWillUpdate = nextProps => {
    if (nextProps.postalCodeInput !== this.props.postalCodeInput) {
      this.setAddressInfo(nextProps.postalCodeInput);
    }
  };

  render() {
    return (
      <div
        className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
        data-widget-edit-button="false"
        id={this.props.id}
      >
        <header role="heading">
          <h2>
            {this.props.title}
          </h2>
        </header>
        <div>
          <div className="widget-body">
            {!this.props.isInstantRate
              ? <div>
                  {this.showAddressBook(this.props.hasAddressBookData)}
                  {' '}
                  {this.showSelectedAddress(this.state.selectedAddress)}
                  <hr className="simple" />
                  <Row>
                    <Col className="col" sm={5}>
                      <label className="radio" style={{ whiteSpace: 'nowrap' }}>
                        <input
                          type="radio"
                          name={this.props.name + '-radio'}
                          data-value="new-address"
                          defaultChecked={
                            this.state.isNewAddress ||
                              !this.props.hasAddressBookData
                          }
                          onClick={this.toggleNewAddressFields}
                        />
                        <i />
                        New Address
                      </label>
                    </Col>
                    <Col className="col" sm={7}>
                      {' '}
                      {this.state.isNewAddress &&
                        <label
                          className="checkbox"
                          style={{ whiteSpace: 'nowrap' }}
                        >
                          <Field
                            component="input"
                            type="checkbox"
                            name="saveToAddressBook"
                          />
                          <i />
                          Save to address book
                        </label>}
                    </Col>
                  </Row>
                  <div>
                    {this.showNewAddressFields(this.state.isNewAddress)}
                  </div>
                  {this.props.showDistributionListOption &&
                    <div>
                      <hr className="simple" />
                      <Row>
                        <Col className="col" sm={5}>
                          <label
                            className="radio"
                            style={{ whiteSpace: 'nowrap' }}
                          >
                            <input
                              type="radio"
                              name={this.props.name + '-radio'}
                              data-value="distribution"
                              checked={this.state.isDistributionList}
                              onChange={this.toggleNewAddressFields}
                            />
                            <i />
                            Distribution List
                          </label>
                        </Col>
                        <Col className="col" sm={7}>
                          {' '}
                          {this.state.isDistributionList &&
                            this.showDistributionListFields()}
                        </Col>
                      </Row>
                    </div>}
                  <hr />
                  <br/>
                  <label className="checkbox" style={{ whiteSpace: 'nowrap' }}>
                    <Field
                      component="input"
                      type="checkbox"
                      name="emailNotification"
                    />
                    <i />
                    Notify by email
                  </label>
                </div>
              : this.showInstantAddressFields()}
          </div>
        </div>
      </div>
    );
  }

  createSelectCountries = () => {
    let items = [];

    for (let index in countries) {
      if (countries.hasOwnProperty(index)) {
        items.push(
          <option key={index} value={index}>
            {countries[index]}
          </option>,
        );
      }
    }
    return items;
  };

  createSelectProvinces = selectedCountry => {
    let items = [];

    if (selectedCountry === 'CA' || !selectedCountry) {
      for (let index in provinces) {
        if (provinces.hasOwnProperty(index)) {
          items.push(
            <option key={index} value={index}>
              {provinces[index]}
            </option>,
          );
        }
      }
    } else {
      for (let index in states) {
        if (states.hasOwnProperty(index)) {
          items.push(
            <option key={index} value={index}>
              {states[index]}
            </option>,
          );
        }
      }
    }

    return items;
  };

  setAddressInfo = postalCode => {
    const isUsa = /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(postalCode);
    const isCa = /^[A-Za-z]\d[A-Za-z][ -]?\d[A-Za-z]\d$/.test(postalCode);

    if (isUsa || isCa) {
      fetch(
        'https://maps.googleapis.com/maps/api/geocode/json?sensor=true&key=AIzaSyDwuavQajnKC2lKcL0RKV7dANUtRhl8qHM&address=' +
          postalCode,
      )
        .then(response => response.json())
        .then(json => {
          for (var component of json.results[0].address_components) {
            if (component.types[0] === 'locality') {
              this.props.change(
                this.props.formName,
                this.props.name + '.city',
                component.long_name,
              );
            }
            if (component.types[0] === 'country') {
              this.props.change(
                this.props.formName,
                this.props.name + '.country',
                component.short_name,
              );
            }
            if (component.types[0] === 'administrative_area_level_1') {
              this.props.change(
                this.props.formName,
                this.props.name + '.province',
                component.short_name,
              );
            }
          }
        });
    }
  };

  toggleNewAddressFields = e => {
    if (e.target.dataset.value === 'distribution') {
      this.setState({
        isNewAddress: false,
        selectedAddress: {},
        isDistributionList: true,
      });
    } else {
      if (this.state.isNewAddress)
        this.props.change(this.props.formName, this.props.name, '');

      this.setState({
        isNewAddress: e.target.dataset.value === 'new-address',
        selectedAddress: {},
        isDistributionList: false,
      });
    }
  };

  showSelectedAddress = address => {
    if (Object.keys(address).length !== 0) {
      return (
        <div className="well" style={{ background: '#fff', marginTop: '5px' }}>
          <div className="padding-10">
            <Row>
              <Col className="col" xs={12} sm={12} md={6}>
                <h4>
                  <b>
                    {address.consigneeName ||
                      address.company ||
                      address.companyName ||
                      ''}
                  </b>
                </h4>
                <address>
                  {(address.residential !== 0) 
                    ? <em>(Residential Address)<br/></em>
                    : ''}
                  {formatAddress(address)}
                </address>
              </Col>
              <Col className="col" xs={12} sm={12} md={6}>
                <h4>
                  {address.contactName || address.attention || ''}
                </h4>
                <address>
                  {address.phone}
                  <span className="email-address">
                    {address.contactEmail}
                  </span>
                </address>
              </Col>
            </Row>
          </div>
        </div>
      );
    }
  };

  processFile = e => {
    this.props.uploadDistributionListFile(this.props.formName, e.target);
  };

  showAddressBook = isVisible => {
    if (isVisible) {
      return (
        <Row>
          <Col className="col" sm={5}>
            <label className="radio">
              <input
                type="radio"
                name={this.props.name + '-radio'}
                data-value="from-addressbook"
                defaultChecked={
                  !this.state.isNewAddress && this.props.hasAddressBookData
                }
                onClick={this.toggleNewAddressFields}
              />
              <i />
              Address Book
            </label>
          </Col>
          <Col className="col" sm={7}>
            <AddressBookAutoSuggest
              ref={autoSuggest => (this.autoSuggest = autoSuggest)}
              disabled={
                this.state.isNewAddress || this.state.isDistributionList
              }
              customer={this.props.customer}
              value={this.state.selectedAddress}
              id={this.props.formName + '-' + this.props.name + '-addressbook'}
              placeholder="Search your address book..."
              onSelection={this.fillFromAddressBook}
              style={{ width: '100%' }}
            />
            <div className="text-muted">
              <small>
                Search your address book using company name, address, zip or
                city
              </small>
            </div>
          </Col>
        </Row>
      );
    }
    return null;
  };

  showDistributionListFields = () => {
    const style = { border: '1px solid #ccc', background: '#fff' };

    switch (this.props.distributionList.status) {
      case 'Uploading':
        return (
          <div className="padding-10" style={style}>
            <div className="loading" />
          </div>
        );

      case 'Error':
        return (
          <div className="padding-10" style={style}>
            <div className="alert alert-danger">
              An Error Occurred. Try again!
            </div>
            <label className="label">Upload CSV File</label>
            <label className="file">
              <input
                type="file"
                name="distributionList"
                accept=".csv"
                onChange={e => this.processFile(e)}
              />
            </label>
          </div>
        );

      case 'Done':
        return (
          <div className="padding-10" style={style}>
            <table className="table table-bordered table-condensed">
              <tbody>
                <tr>
                  <td>Total Entries</td>
                  <td>
                    {this.props.distributionList.totalEntries}
                  </td>
                </tr>
                <tr>
                  <td>Errors</td>
                  <td className="text-danger">
                    {this.props.distributionList.errors}
                  </td>
                </tr>
                <tr>
                  <td>Recipients</td>
                  <td>
                    {this.props.distributionList.processed}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        );

      default:
        return (
          <div className="padding-10" style={style}>
            <label className="label">Upload CSV File</label>
            <label className="file">
              <input
                type="file"
                name="distributionList"
                accept=".csv"
                onChange={e => this.processFile(e)}
              />
            </label>
          </div>
        );
    }
  };

  showNewAddressFields = isVisible => {
    if (isVisible) {
      return (
        <div>
          <br />
          <Row>
            <Col className="col" sm={12}>
              <InputText name="consigneeName" label="Company Name" required />
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={6}>
              <InputText name="address1" label="Address Line 1" required />
            </Col>
            <Col className="col" sm={6}>
              <InputText name="address2" label="Address Line 2" />
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={8}>
              <InputText name="city" label="City" required />
            </Col>
            <Col className="col" sm={4}>
              <InputText name="postalCode" label="Postal Code" required />
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={6}>
              <InputSelect name="province" label="Province">
                {this.createSelectProvinces(this.props.selectedCountry)}
              </InputSelect>
            </Col>
            <Col className="col" sm={6}>
              <InputSelect name="country" label="Country">
                {this.createSelectCountries()}
              </InputSelect>
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={4}>
              <InputText name="contactName" label="Contact Name" required />
            </Col>
            <Col className="col" sm={4}>
              <InputText
                name="phone"
                label="Phone"
                parse={value => formatPhone(value)}
                required
              />
            </Col>
            <Col className="col" sm={4}>
              <InputText name="email" label="Email" />
            </Col>
            <Col className="col" sm={12}>
              <div style={{ paddingTop: '5px', paddingBottom: '15px'}}>
                <label
                  className="checkbox"
                  style={{ whiteSpace: 'nowrap' }}
                >
                  {' '}
                  <Field
                    component="input"
                    type="checkbox"
                    name="residential"
                    format={value => {
                      return value ? true : false;
                    }}
                    parse={value => {
                      return value ? 1 : 0;
                    }}
                  />
                  <i />
                  Residential
                </label>
              </div>
            </Col>
          </Row>
        </div>
      );
    }
  };

  showInstantAddressFields = () => {
    return (
      <div>
        <Row>
          <Col className="col" sm={6}>
            <InputSelect name="country" label="Country">
              {this.createSelectCountries()}
            </InputSelect>
          </Col>
          <Col className="col" sm={6}>
            <InputText name="postalCode" label="Postal Code" required />
          </Col>
        </Row>
        <Row>
          <Col className="col" sm={6}>
            <InputSelect name="province" label="Province">
              {this.createSelectProvinces(this.props.selectedCountry)}
            </InputSelect>
          </Col>
          <Col className="col" sm={6}>
            <InputText name="city" label="City" />
          </Col>
        </Row>
      </div>
    );
  };

  fillFromAddressBook = addressBookEntry => {
    // this is being called by the autoSuggest onSelection
    if (Object.keys(addressBookEntry).length !== 0) {
      // The id here is actually the addressBookId...
      // Need to replace id to null and assign id to addressBookId
      const newAddressBookEntry = Object.assign({}, addressBookEntry, {
        addressBookId: addressBookEntry.id,
        id: null,
      });
      this.props.change(
        this.props.formName,
        this.props.name,
        newAddressBookEntry,
      );
    } else {
      this.props.change(this.props.formName, this.props.name, '');
    }

    this.setState({ selectedAddress: addressBookEntry });
  };
}

const mapStateToProps = (state, ownProps) => {
  const formName = state.tabs.activeTab;
  const selector = formValueSelector(formName);
  const selectedCountry = selector(state, ownProps.name + '.country');
  const postalCodeInput = selector(state, ownProps.name + '.postalCode');
  const { isInstantRate = false } = selectActiveTab(state);

  return {
    distributionList: selectDistributionList(state, formName),
    selectedCountry,
    postalCodeInput,
    isInstantRate,
    formName,
    selectedAddress: selector(state, ownProps.name),
  };
};

const mapDispatchToProps = { change, uploadDistributionListFile };

export default connect(mapStateToProps, mapDispatchToProps)(ShipmentAddress);
