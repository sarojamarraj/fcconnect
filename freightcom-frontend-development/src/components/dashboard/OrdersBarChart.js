import React, { Component } from 'react';
import { Bar } from 'react-chartjs-2';
import { connect } from 'react-redux';
import { Button } from 'react-bootstrap';
import moment from 'moment';

import { makeCancelable } from '../../utils';

class OrdersBarChart extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: { labels: [], datasets: [] },
      chartOption: 'daily',
      chartOptions: { scales: { yAxes: [{ ticks: { beginAtZero: true } }] } },
    };

    // This is for the cancellable fetch request
    this.fetchRequest = null;
  }

  componentWillMount() {
    this.getOrderCounts(
      moment().add(-6, 'd').format('YYYY-MM-DD'),
      moment().format('YYYY-MM-DD'),
      this.state.chartOption,
    );
  }

  componentWillUnmount() {
    this.fetchRequest.cancel();
  }

  setChartData = (option, data) => {
    const chartData = {
      labels: [],
      datasets: [
        {
          label: option === 'weekly' ? 'Weekly Shipments' : 'Daily Shipments',
          backgroundColor: 'rgba(255,99,132,0.2)',
          borderColor: 'rgba(255,99,132,1)',
          borderWidth: 1,
          hoverBackgroundColor: 'rgba(255,99,132,0.4)',
          hoverBorderColor: 'rgba(255,99,132,1)',
          data: [],
        },
      ],
    };
    for (var value of data) {
      if (option === 'daily') {
        chartData.labels.push(moment(value.label).format('MMM, D'));
      } else {
        const weekOfYear = parseInt(value.label.slice(-2), 10);
        const startOfWeek = moment()
          .day('Sunday')
          .week(weekOfYear)
          .format('MMM, D');
        const endOfWeek = moment()
          .day('Saturday')
          .week(weekOfYear)
          .format('MMM, D');
        const weekLabel = `${startOfWeek} - ${endOfWeek}`;
        chartData.labels.push(weekLabel);
      }
      chartData.datasets[0].data.push(parseInt(value.count, 10));
    }
    this.setState({ data: chartData, chartOption: option });
  };

  getOrderCounts = (startDate, endDate, option) => {
    const { activeRole } = this.props;
    let fetchURL = null;
    if (
      activeRole.roleName === 'CUSTOMER_ADMIN' ||
      activeRole === 'CUSTOMER_STAFF'
    ) {
      fetchURL = option === 'weekly'
        ? `/api/orders-by-week?customerId=${activeRole.customerId}&from=${startDate}&to=${endDate}`
        : `/api/orders-by-day?customerId=${activeRole.customerId}&from=${startDate}&to=${endDate}`;
    } else {
      fetchURL = option === 'weekly'
        ? `/api/orders-by-week?from=${startDate}&to=${endDate}`
        : `/api/orders-by-day?from=${startDate}&to=${endDate}`;
    }

    // Need to wrap the fetch request to a cancellable promise so that
    // we can cancel the setState if the component is unmounted already.
    this.fetchRequest = makeCancelable(
      fetch(fetchURL, {
        method: 'GET',
        credentials: 'include',
      }).then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Network response was not ok.');
      }),
    );

    this.fetchRequest.promise
      .then(json => {
        this.setChartData(option, json);
      })
      .catch(error => {
        if (error.message) console.log('Error: ' + error.message);
      });
  };

  render() {
    return (
      <div className="col-sm-6">
        <div
          className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
          id="wid-id-0"
          data-widget-editbutton="false">
          <header role="heading">
            <span className="widget-icon">
              <i className="fa fa-bar-chart-o" />
            </span>
            <h2>Monthly Shipment Analytics</h2>
            <span className="jarviswidget-loader">
              <i className="fa fa-refresh fa-spin" />
            </span>
          </header>
          <div>
            <div>
              <div className="widget-body no-padding">
                <div className="well no-border">
                  <div className="row">
                    <div className="col-xs-12">
                      <Button
                        bsStyle="default"
                        bsSize="xsmall"
                        className="pull-right"
                        style={{ marginRight: '5px' }}
                        onClick={e => {
                          e.preventDefault();
                          if (this.state.chartOption === 'daily') {
                            this.getOrderCounts(
                              moment().add(-30, 'd').format('YYYY-MM-DD'),
                              moment().format('YYYY-MM-DD'),
                              'weekly',
                            );
                          } else {
                            this.getOrderCounts(
                              moment().add(-6, 'd').format('YYYY-MM-DD'),
                              moment().format('YYYY-MM-DD'),
                              'daily',
                            );
                          }
                        }}>
                        {this.state.chartOption === 'weekly'
                          ? 'Daily'
                          : 'Weekly'}
                      </Button>
                    </div>
                  </div>
                  <div id="bar-chart">
                    <Bar
                      data={this.state.data}
                      width={100}
                      height={50}
                      options={this.state.chartOptions}
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => {
  const { loggedIn: { role = {} } } = state;
  return { activeRole: role };
};

export default connect(mapStateToProps)(OrdersBarChart);
