import React from 'react';
import PropTypes from 'prop-types';

const Widget = ({ title, children }) =>
  <div
    className="jarviswidget jarviswidget-color-blueLight"
    id={title.toString().toLowerCase().replace(/\s+/g, '-')}
    data-widget-colorbutton="false"
    data-widget-editbutton="false"
    data-widget-togglebutton="false"
    data-widget-deletebutton="false"
    data-widget-fullscreenbutton="false"
    data-widget-custombutton="true"
    data-widget-sortable="false"
  >
    {/* <h1>{title}</h1> */}
    <div>
      <div className="widget-body">
        {children}
      </div>
    </div>
  </div>;

Widget.defaultProps = {
  title: PropTypes.string.isRequired,
};

export default Widget;
