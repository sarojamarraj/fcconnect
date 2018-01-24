import React, { Component } from 'react';
import { connect } from 'react-redux';

class CreditCardSelector extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showCCFields: false,
      showCCList: false,
      ccInfo: {},
    };
  }

  render() {
    return (
      <div className="row">
        <div className="col-sm-12 col-xs-12">
          <label className="radio radio-inline">
            <input
              type="radio"
              className="radiobox"
              name="useNew"
              value="new"
              onChange={e => {
                this.toggleCCOption(e);
                this.updateCCInfo(e);
              }}
              ref="useNew"
            />
            <span> Use new credit card</span>
          </label>
          <label className="radio radio-inline">
            <input
              type="radio"
              className="radiobox"
              name="useNew"
              value="existing"
              onChange={e => {
                this.toggleCCOption(e);
                this.updateCCInfo(e);
              }}
              ref="useExisting"
            />
            <span>Select a credit card from the vault</span>
          </label>
        </div>
        <br />
        <br />
        {this.state.showCCFields && this.renderCCFields()}
        {this.state.showCCList && this.renderCCList()}
      </div>
    );
  }

  renderCCFields = () => {
    return (
      <div key="ccFields" className="col-sm-12 col-xs-12">
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label>Card Type</label>
              <label>
                <select name="ccType" onChange={this.updateCCInfo}>
                  <option />
                  <option value="visa">Visa</option>
                  <option value="visa">Master Card</option>
                </select>
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <section className="form-group">
              <label>Name on card</label>
              <label>
                <input
                  type="text"
                  name="nameOnCard"
                  onChange={this.updateCCInfo}
                />
              </label>
            </section>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label>Card Number</label>
              <label>
                <input
                  type="text"
                  name="creditCardNumber"
                  onChange={this.updateCCInfo}
                />
              </label>
            </section>
          </div>
          <div className="col-sm-2">
            <section className="form-group">
              <label>Expiry</label>
              <label>
                <select name="expiryMonth" onChange={this.updateCCInfo}>
                  <option />
                  <option value="01">01</option>
                  <option value="02">02</option>
                  <option value="03">03</option>
                  <option value="04">04</option>
                  <option value="05">05</option>
                  <option value="06">06</option>
                  <option value="07">07</option>
                  <option value="08">08</option>
                  <option value="09">09</option>
                  <option value="10">10</option>
                  <option value="11">11</option>
                  <option value="12">12</option>
                </select>
              </label>
            </section>
          </div>
          <div className="col-sm-2">
            <section className="form-group">
              <label><span style={{ color: 'white' }}>Year</span></label>
              <label>
                <select name="expiryYear" onChange={this.updateCCInfo}>
                  <option />
                  <option value="17">2017</option>
                  <option value="18">2018</option>
                  <option value="19">2019</option>
                  <option value="20">2020</option>
                  <option value="21">2021</option>
                  <option value="22">2022</option>
                  <option value="23">2023</option>
                  <option value="24">2024</option>
                  <option value="25">2025</option>
                  <option value="26">2026</option>
                </select>
              </label>
            </section>
          </div>
          <div className="col-sm-2">
            <section className="form-group">
              <label>CVS</label>
              <label>
                <input type="text" name="cvs" onChange={this.updateCCInfo} />
              </label>
            </section>
          </div>
        </div>
      </div>
    );
  };

  renderCCList = () => {
    const { customer } = this.props;
    if (customer.creditCards && customer.creditCards.length > 0) {
      return (
        <div key="list" className="col-sm-12 col-xs-12">
          <div className="col-sm-8">
            <table>
              <tbody>
                {customer.creditCards.map((cc, index) => {
                  const key = 'cc' + index;

                  return (
                    <tr key={key}>
                      <td width="10px">
                        <label className="radio radio-inline">
                          <input
                            id={key}
                            type="radio"
                            className="radiobox"
                            name="cardIndex"
                            value={index}
                            onChange={this.updateCCInfo}
                          />
                          <span />
                        </label>
                      </td>
                      <td width="100px">
                        <label htmlFor={key}> {cc.number},</label>
                      </td>
                      <td width="65px">
                        <label htmlFor={key}>
                          {cc.expiryMonth}/{cc.expiryYear},
                        </label>
                      </td>
                      <td width="180px">
                        <label htmlFor={key}>
                          {cc.name ? cc.name.toUpperCase() : 'XXXX'}
                        </label>
                      </td>

                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      );
    } else {
      return (
        <div className="row">
          <div className="col-sm-12 col-xs-12">
            <ul>
              <li>No credit card available...</li>
            </ul>
          </div>
        </div>
      );
    }
  };

  toggleCCOption = e => {
    if (e.target.value === 'new') {
      this.setState({
        showCCFields: true,
        showCCList: false,
      });
    }
    if (e.target.value === 'existing') {
      this.setState({
        showCCList: true,
        showCCFields: false,
      });
    }
  };

  setCCInfo = ccInfo => {
    this.setState({ ccInfo });
    this.props.onChange(ccInfo);
  };

  updateCCInfo = e => {
    const { target: { name, value } = {} } = e;

    if (name === 'useNew') {
      this.setCCInfo({
        [name]: value,
      });
    } else {
      let { ccInfo } = this.state;

      this.setCCInfo({ ...ccInfo, [name]: value });
    }
  };
}

export default connect(null, null)(CreditCardSelector);
