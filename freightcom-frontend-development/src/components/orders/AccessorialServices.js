import React from 'react';
import { connect } from 'react-redux';
import { Field, FormSection } from 'redux-form';

import { loadAccessorialServices } from '../../actions/orders';
import { getAccessorialServices } from '../../reducers';

class AccessorialServices extends FormSection {
  componentWillMount() {
    this.props.loadAccessorialServices();
  }

  render() {
    return (
      <div>
        <h3 className="well-sub-title">Additional Services</h3>
        <hr className="simple" />
        <div className="row">
          {this.props.items.map(this.renderItem)}
        </div>
      </div>
    );
  }

  renderItem(item, index) {
    return (
      <div key={'additional-services-' + index} className="col col-sm-3">
        <label className="checkbox">
          <Field name={item.name} type="checkbox" component="input" />{' '}
          {item.name} <i />
        </label>
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  return {
    items: getAccessorialServices(state, ownProps.type),
  };
};

export default connect(mapStateToProps, { loadAccessorialServices })(
  AccessorialServices,
);
