import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, Button, ButtonToolbar } from 'react-bootstrap';
import { addNotification } from 'reapop';
import Icon from '../components/Icon';
import { formatAddress } from '../utils';

import { getPageItems } from '../reducers';
import {
  loadAddressBook,
  saveAddressBookEntry,
  deleteAddressBookEntry,
} from '../actions/addressBook';

import Widget from '../components/Widget';
import DataTable from '../components/DataTable';

import AddressBookForm from '../containers/addressBooks/AddressBookForm';
import AlertMessage from '../components/AlertMessage';

class AddressBook extends Component {
  constructor(props) {
    super(props);
    this.state = { showModal: false, addressBookId: 'new', showAlert: false };

    this.addressBookList = null;
    this.columns = [
      {
        width: '180',
        label: 'Company',
        field: 'consigneeName',
        cell: item => <b>{item.consigneeName}</b>,
      },
      {
        label: 'Address',
        field: 'address1',
        cell: ({ address1, address2 }) => formatAddress({ address1, address2 }),
      },
      { width: '80', label: 'City', field: 'city' },
      { width: '80', label: 'Province', field: 'province' },
      { width: '80', label: 'Country', field: 'country' },
      { width: '80', label: 'Postal/Zip', field: 'postalCode' },
      { width: '80', label: 'Name', field: 'contactName' },
      { width: '80', label: 'Phone', field: 'phone' },
      { width: '80', label: 'Email', field: 'contactEmail' },
      {
        width: '20',
        cell: item =>
          <div className="btn-group flex">
            <Button
              onClick={e => this.showModal(item.id)}
              className="btn btn-primary btn-xs">
              <Icon name="pencil" /> Edit
            </Button>
            <Button
              bsStyle="primary"
              bsSize="xs"
              className="dropdown-toggle"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false">
              <span className="caret" />
              <span className="sr-only">Toggle Dropdown</span>
            </Button>
            <ul className="dropdown-menu dropdown-menu-right">
              <li>
                <a href="#" onClick={e => this.showModal(item.id)}>
                  <Icon name="pencil" /> Edit
                </a>
              </li>
              <li>
                <a
                  href="#"
                  onClick={e =>
                    this.setState({
                      showAlert: true,
                      addressBookId: item.id,
                    })}>
                  <Icon name="trash" /> Delete
                </a>
              </li>
            </ul>
          </div>,
      },
    ];
  }

  componentWillMount() {
    this.props.loadAddressBook();
  }

  render() {
    const { addressBookEntity } = this.props;

    return (
      <Widget title="Address Book">
        <DataTable
          columns={this.columns}
          loadData={this.props.loadAddressBook}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          ref={datatable => (this.addressBookList = datatable)}
          groupedFilters
          actionButtons={
            <Button
              bsStyle="primary"
              bsSize="small"
              onClick={e => this.showModal('new')}>
              Add a Contact
            </Button>
          }
        />
        <Modal show={this.state.showModal} onHide={this.hideModal}>
          <Modal.Header closeButton>
            <Modal.Title>
              {this.state.addressBookId === 'new'
                ? 'Add New Address'
                : 'Edit Address'}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <AddressBookForm
              id={this.state.addressBookId}
              initialValues={addressBookEntity[this.state.addressBookId]}
              close={this.hideModal}
              onSubmit={this.submit}
            />
          </Modal.Body>
        </Modal>
        {this.state.showAlert &&
          <AlertMessage
            title="Delete Address Entry"
            text="Are you sure to remove this address entry?"
            buttonToolbar={
              <ButtonToolbar>
                <Button
                  onClick={e => {
                    e.preventDefault();
                    this.setState({
                      showAlert: false,
                      addressBookId: 'new',
                    });
                  }}>
                  Cancel
                </Button>
                <Button
                  bsStyle="primary"
                  onClick={e => {
                    e.preventDefault();
                    this.deleteAddress();
                  }}>
                  Delete
                </Button>
              </ButtonToolbar>
            }
          />}
      </Widget>
    );
  }

  showModal = id => {
    this.setState({ showModal: true, addressBookId: id });
  };

  hideModal = () => {
    this.setState({ showModal: false, addressBookId: 'new' });
  };

  submit = form => {
    const { addressBookId } = this.state;
    const values = { ...form, customerId: this.props.customerId };

    const callback = apiPayload => {
      if (!apiPayload.error) {
        this.props.addNotification({
          message: 'Address book entry saved.',
          status: 'success',
        });
        this.addressBookList.loadData();
      } else {
        this.props.addNotification({
          message: 'There was an error saving address book entry',
          status: 'error',
        });
      }
      this.hideModal();
    };

    if (addressBookId === 'new') {
      this.props.saveAddressBookEntry(values).then(callback);
    } else {
      this.props.saveAddressBookEntry(values, addressBookId).then(callback);
    }
  };

  deleteAddress = () => {
    this.props.deleteAddressBookEntry(
      this.state.addressBookId,
      response => {
        this.setState({
          showAlert: false,
          addressBookId: 'new',
        });
        this.props.addNotification({
          message: 'Address book entry removed.',
          status: 'success',
        });
        this.props.loadAddressBook(this.props.currentPage);
      },
      resoponse => {
        this.setState({
          showAlert: false,
          addressBookId: 'new',
        });
        this.props.addNotification({
          message: 'There was an error deleting address book entry',
          status: 'error',
        });
      },
    );
  };
}

const mapStateToProps = state => {
  const { entities: { addressBook }, loggedInUser: { customerId } } = state;

  return {
    ...getPageItems(state, 'addressBook'),
    customerId,
    addressBookEntity: addressBook,
  };
};

const mapDispatchToProps = {
  loadAddressBook,
  saveAddressBookEntry,
  deleteAddressBookEntry,
  addNotification,
};

export default connect(mapStateToProps, mapDispatchToProps)(AddressBook);
