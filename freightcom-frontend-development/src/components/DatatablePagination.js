import React, { Component } from 'react';
import { Pagination } from 'react-bootstrap';

class DatatablePagination extends Component {
  render() {
    const {
      numberOfPages = 1,
      currentPage = 1,
      totalItems = 1,
      itemsPerPage = 10,
      showCount = false,
    } = this.props;

    const itemPosition = parseInt(currentPage * itemsPerPage, itemsPerPage);

    return (
      <div className="clearfix">
        {showCount &&
          <div className="pull-left">
            <div
              style={{
                margin: '20px 0',
                fontSize: '15px',
              }}
            >
              Showing{' '}
              <strong>
                {itemPosition - itemsPerPage + 1}
                {' '}to{' '}
                {totalItems > itemPosition ? itemPosition : totalItems}
              </strong>
              {' '}of{' '}
              <strong>{totalItems}</strong>
              {' '}results
            </div>
          </div>}
        <div className="pull-right text-right">
          <Pagination
            items={parseInt(numberOfPages, 10)}
            onSelect={this.props.paginate}
            activePage={parseInt(currentPage, 10)}
            maxButtons={10}
            prev
            next
            ellipsis
            boundaryLinks
          />
        </div>
      </div>
    );
  }
}

export default DatatablePagination;
