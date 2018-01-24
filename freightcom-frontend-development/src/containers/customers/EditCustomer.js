import React, { Component } from 'react';
import { connect } from 'react-redux';
import Widget from '../../components/Widget';
import EditCustomerForm from '../../components/customers/EditCustomerForm';

import { selectOneFromEntities } from '../../reducers';
import { loadCustomerById } from '../../actions/customers';

class EditCustomer extends Component {
  componentWillMount() {
    const { loadCustomerById, customerId } = this.props;

    if (customerId) {
      loadCustomerById(customerId);
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.customerId !== this.props.customerId) {
      this.props.loadCustomerById(nextProps.customerId);
    }
  }

  render() {
    const {
      customer: { id, name } = {},
      routeParams: { tab = false } = {},
    } = this.props;

    const pageTitle = <span>Edit Customer: <b>{name}</b></span>;

    return (
      <Widget title={pageTitle}>
        <EditCustomerForm customerId={id} tab={tab} />
      </Widget>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  const customerId = ownProps.params['id'] || 0;
  return {
    customerId,
    customer: selectOneFromEntities(state, 'customer', customerId),
  };
};

export default connect(mapStateToProps, { loadCustomerById })(EditCustomer);
