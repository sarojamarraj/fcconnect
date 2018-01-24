import React, { PureComponent } from 'react';
import { findDOMNode } from 'react-dom';
import { connect } from 'react-redux';
import { change } from 'redux-form';
import startCase from 'lodash/startCase';
import {
  Row,
  Col,
  Glyphicon,
  Alert,
  Popover,
  OverlayTrigger,
} from 'react-bootstrap';

import {
  selectActiveTab,
  selectOrderRates,
  selectOrderMessages,
  checkIfRatesFetching,
  checkIfRatesError,
  checkIfRatesFetchingMore,
  checkIfRatesDirty,
  checkIfRatesLoaded,
  getSelectedCarrier,
} from '../../reducers';

import { requestShipmentRates } from '../../actions/orders';

class Rates extends PureComponent {
  constructor(props) {
    super(props);
    this.state = { sorting: 'totalCharges' };
    this.shouldScroll = true;
    this.showDirtyNotification = false;
  }

  componentWillReceiveProps(nextProps) {
    if (!this.props.isLoaded && nextProps.isLoaded) {
      // This is invoked when rates has been loaded and got dirty...
      this.shouldScroll = true;
      this.props.change(this.props.formName, 'carrierId', '');
    }

    if (this.props.isLoaded && !this.props.isDirty && nextProps.isDirty) {
      // Rates has been loaded and got marked dirty
      this.showDirtyNotification = true;
    }

    if (nextProps.isLoaded && this.props.isDirty && !nextProps.isDirty) {
      this.showDirtyNotification = false;
    }
  }

  componentDidUpdate() {
    if (this.props.rates.length > 0) {
      this.scrollToTop();
    }
  }

  render() {
    if (this.props.isFetching) {
      return (
        <div className="padding-10">
          <div className="loading" />
        </div>
      );
    } else if (this.props.errorFetching) {
      return (
        <Alert bsStyle="danger">
          <div>{this.props.errorFetching}</div>
        </Alert>
      );
    }

    if (this.props.isLoaded && !this.props.isDirty) {
      return (
        <div
          className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
          data-widget-edit-button="false"
        >
          <header role="heading">
            <h2>{'Carrier Rates'}</h2>
          </header>
          <div>
            <div className="widget-body">
              <div className="container sort-rates">
                <label className="select" style={{ width: '160px' }}>
                  <select
                    onChange={e => this.setState({ sorting: e.target.value })}
                  >
                    <option value="totalCharges">Sort by cost</option>
                    <option value="transitDays">Sort by days</option>
                  </select>{' '}
                  <i />
                </label>
                {this.props.isFetchingMore
                  ? <div className="padding-10">
                      <div className="loading" />
                    </div>
                  : <div className="text-right">
                      <a
                        className="btn btn-primary btn-lg"
                        onClick={e => {
                          e.preventDefault();
                          this.props.requestShipmentRates(
                            this.props.formName,
                            this.props.orderId,
                            true,
                          );
                        }}
                      >
                        <Glyphicon glyph="refresh" />
                        {' Refresh'}
                      </a>
                      <br />
                      <br />
                    </div>}
              </div>
              {this.renderRatesMessage()}
              {this.renderServiceMessages()}
              <div className="row-fluid">
                {(this.props.rates.length !== 0) ? this.props.rates
                  .sort((a, b) => {
                    return (
                      parseFloat(a[this.state.sorting]) -
                      parseFloat(b[this.state.sorting])
                    );
                  })
                  .map(this.renderCarrier) : 
                  this.renderNoRates()
                  }
              </div>
            </div>
          </div>
        </div>
      );
    }

    return (
      <div>
        {this.renderRatesDirtyMessage()}
      </div>
    );
  }

  renderNoRates = () => {
    return (
      <Col style={{ marginBottom: '10px', position: 'relative'}}
      className="carrier-container no-rates padding-10 text-center col"
      sm={12}
      xs={12}>
      <h2>
        <i className="fa fa-warning"></i>
        {' '} No available rates were found
      </h2>
      {'Please review your shipment details'}
    </Col>
    );
  };

  renderCarrier = (item, index) => {
    const isSelectedRate = this.props.selectedRate === item.id;
    const selectedClass = isSelectedRate ? 'alert alert-success invert ' : '';
    const {
      id,
      carrierName,
      logo = '//placehold.it/120x60',
      service,
      serviceName,
      description,
      transitDays,
      currency,
      charges,
      totalCharges,
    } = item;
    return (
      <Col
        key={'carrier-' + id}
        style={{ marginBottom: '10px', position: 'relative' }}
        className={selectedClass + 'carrier-container col'}
        onClick={e => {
          this.setCarrier(id);
        }}
        sm={12}
        xs={12}
      >
        <div className="carrier-rate">
          <Row className="flex">
            <Col className="col" md={1} sm={12} xs={12}>
              <div className="carrier carrier-logo text-center">
                <img src={logo} alt="" className="img-responsive" />
              </div>
            </Col>
            <Col className="col" md={3} sm={12} xs={12}>
              <div className="service-name">
                <h4>
                  {serviceName}
                </h4>
              </div>
              <div className="carrier hide">
                <h4>
                  {carrierName}
                </h4>
              </div>
            </Col>
            <Col className="col" md={4} sm={12} xs={12}>
              <div className="carrier-description">
                {description}
              </div>
            </Col>
            <Col className="col" md={2} sm={12} xs={12}>
              <div className="timeframe">
                <h4>
                  <b>
                    {service}
                  </b>
                </h4>
                <h5 className="cal-head">Est Transit Days</h5>
                <h3 className="days">
                  {(Number(transitDays) > 1) 
                    ? transitDays + ' days':
                      (!transitDays)
                    ? '0 days':
                    transitDays + ' day'
                  }
                </h3>
              </div>
            </Col>
            <Col className="col" md={2} sm={12} xs={12}>
              <div className="fees hidden">
                <table className="table table-bordered table-condensed">
                  <tbody>
                    {Object.keys(charges).map(key => (
                      <tr key={key}>
                        <td>
                          {startCase(key)}
                        </td>
                        <td>
                          {item.charges[key]}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <div className="total">
                <h4 style={{ display: 'none' }}>Total Amount</h4>
                <h3 className="amount">
                  {`${(totalCharges || 0).toFixed(2).toLocaleString()}`}
                  <span>
                    {' '} {currency}
                  </span>
                  <OverlayTrigger
                    trigger={['hover', 'focus']}
                    placement="left"
                    overlay={
                      <Popover
                        id={`popover-rates-${id}`}
                        title="Cost Breakdown"
                      >
                        <table className="table _table-bordered table-condensed">
                          <tbody>
                            {Object.keys(charges).map(key => (
                              <tr key={key}>
                                <td>
                                  {startCase(key)}
                                </td>
                                <td style={{ textAlign: 'right' }}>
                                  {item.charges[key]
                                    ? item.charges[key]
                                        .toFixed(2)
                                        .toLocaleString()
                                    : '0.00'}
                                </td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </Popover>
                    }
                  >
                    <Glyphicon glyph="info-sign" />
                  </OverlayTrigger>
                </h3>
              </div>
            </Col>
          </Row>
          {isSelectedRate
            ? <div
                className="check"
                style={{
                  position: 'absolute',
                  top: '0px',
                  right: '0px',
                  color: '#fff',
                  background: '#4CAF50',
                  padding: '2px 2px 2px 4px',
                  borderBottomLeftRadius: '4px',
                }}
              >
                <Glyphicon glyph="ok" />
              </div>
            : ''}
        </div>
      </Col>
    );
  };

  renderRatesMessage = () => {
    return (
      <Alert bsStyle="info">
        <Glyphicon glyph="info" />
        <strong>Info!</strong> Any changes in the form will reset the rates.
      </Alert>
    );
  };

  renderServiceMessages = () => {
    return (
      <Alert bsStyle="info">
        <Glyphicon glyph="info" />
        <strong>Info!</strong>
        {this.props.serverMessages.map((message, index) => (
          <div key={index}>{message}</div>
        ))}
      </Alert>
    );
  };

  renderRatesDirtyMessage = () => {
    if (this.showDirtyNotification) {
      return (
        <Alert bsStyle="warning">
          <Glyphicon glyph="warning" />
          Form data has been changed. You need to request rates again.
        </Alert>
      );
    }
  };

  setCarrier = id => {
    this.props.change(this.props.formName, 'carrierId', id);
  };

  scrollToTop = () => {
    if (this.shouldScroll) {
      const node = findDOMNode(this);
      window.scrollTo(0, node.offsetTop);
      this.shouldScroll = false;
    }
  };
}

const mapStateToProps = state => {
  const tab = selectActiveTab(state);
  const formName = tab.id;
  const orderId = tab.orderId || false;
  return {
    formName,
    orderId,
    errorFetching: checkIfRatesError(state, formName),
    isFetching: checkIfRatesFetching(state, formName),
    isFetchingMore: checkIfRatesFetchingMore(state, formName),
    isDirty: checkIfRatesDirty(state, formName),
    isLoaded: checkIfRatesLoaded(state, formName),
    rates: selectOrderRates(state, formName),
    serverMessages: selectOrderMessages(state, formName),
    selectedRate: getSelectedCarrier(state, formName),
  };
};

const mapDispatchToProps = { change, requestShipmentRates };

export default connect(mapStateToProps, mapDispatchToProps)(Rates);
