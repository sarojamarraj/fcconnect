import React from 'react';

const style = {
  badge: {
    fontSize: '18px',
    margin: '0 6px 0 0',
  },
  lastItem: {
    borderRight: '1px solid #ddd',
  },
};

const ShipmentWizardRibbon = ({ step, orderId }) =>
  <div className="fuelux">
    <div className="wizard">
      <ul className="steps">
        <li className={step === '1' ? 'active' : ''}>
          <span className="" style={style.badge}>1</span>Get Rates<span className="chevron" />
        </li>
        <li className={step === '2' ? 'active' : ''}>
          <span className="" style={style.badge}>2</span>Confirm Details and
          Book<span className="chevron" />
        </li>
        <li className={step === '3' ? 'active' : ''} style={style.lastItem}>
          <span className="" style={style.badge}>3</span>Track Shipment
          <span className="chevron" />
        </li>
      </ul>
      {orderId &&
        step !== '3' &&
        <div className="actions">
          <h2
            style={{
              margin: '0',
              padding: '0',
              color: '#666',
              height: '48px',
              lineHeight: '48px',
              fontSize: '18px',
            }}
          >
            {step === '3' ? 'Shipment: ' : 'Quote: '}
            <b>{orderId}</b>
          </h2>
        </div>}
    </div>
  </div>;

export default ShipmentWizardRibbon;
