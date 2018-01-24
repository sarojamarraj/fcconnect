import React, { Component } from 'react';

class CustomsInvoice extends Component {
  render() {
    return (
      <table className="table table-bordered customs-invoice">
        <tbody>
          <tr className="customs-invoice">
            <td>DATE OF SHIPMENT</td>
            <td>REFERENCE</td>
          </tr>
          <tr>
            <td>11/08/2016</td>
            <td>Order ID: 430279 Ref#:T291748</td>
          </tr>
          <tr className="customs-invoice">
            <td>MODE OF TRANSPORT</td>
            <td>TO BE CLEARED BY</td>
          </tr>
          <tr>
            <td />
            <td>Test</td>
          </tr>
          <tr className="customs-invoice">
            <td colSpan="2">Duites and Taxes</td>
          </tr>
          <tr>
            <td colSpan="2">
              Please charge all applicable duties and taxes to receiver
            </td>
          </tr>
          <tr className="customs-invoice">
            <td>SHIPPER</td>
            <td>CONSIGNEE</td>
          </tr>
          <tr>
            <td>
              Redknee Inc
              <br />
              My address
              <br />
              Third Fllor Reception
              <br />
              MISSISSAUGA, CA, ON, L4W4Y9
              <br />
              <br />
              Tax ID:
            </td>
            <td>
              Redknee Inc
              <br />
              My address
              <br />
              Third Fllor Reception
              <br />
              MISSISSAUGA, CA, ON, L4W4Y9
              <br />
              <br />
              {"Recipient's Tax ID:"}
            </td>
          </tr>
          <tr className="customs-invoice">
            <td colSpan="2">BILL TO</td>
          </tr>
          <tr>
            <td colSpan="2">
              Redknee Inc
              <br />
              My address
              <br />
              Third Fllor Reception
              <br />
              MISSISSAUGA, CA, ON, L4W4Y9
              <br />
              <br />
              {"Recipient's Tax ID:"}
            </td>
          </tr>
          <tr className="customs-invoice">
            <td colSpan="2">CONTACT INFORMATION</td>
          </tr>
          <tr>
            <td colSpan="2">
              Redknee Inc<br />My address<br />Third Fllor Reception<br />
            </td>
          </tr>
        </tbody>
      </table>
    );
  }
}

export default CustomsInvoice;
