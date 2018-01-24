import React from 'react';

const Pagination = props => {
  const { currentPage = 1, paginate, itemCount, itemsPerPage = 10 } = props;

  return (
    <ul className="pagination">
      {+currentPage - 1 <= 0
        ? <li className="disabled">
            <a>
              <span
                aria-label="Previous"
                dangerouslySetInnerHTML={{ __html: '&lang;' }}
              />
            </a>
          </li>
        : <li>
            <a
              href="#"
              onClick={e => {
                e.preventDefault();
                e.target.blur();
                paginate(+currentPage - 1);
              }}
            >
              <span
                aria-label="Previous"
                dangerouslySetInnerHTML={{ __html: '&lang;' }}
              />
            </a>
          </li>}
      {itemCount < itemsPerPage
        ? <li className="disabled">
            <a>
              <span
                aria-label="Next"
                dangerouslySetInnerHTML={{ __html: '&rang;' }}
              />
            </a>
          </li>
        : <li>
            <a
              href="#"
              onClick={e => {
                e.preventDefault();
                e.target.blur();
                paginate(+currentPage + 1);
              }}
            >

              <span
                aria-label="Next"
                dangerouslySetInnerHTML={{ __html: '&rang;' }}
              />
            </a>
          </li>}

    </ul>
  );
};

export default Pagination;

/*
export default class Pagination extends Component {

    handleClick(page) {
        this.props.paginate(page);
    }

    render() {
        let pageLinks = [];
        for (let i = 1; i <= this.props.numberOfPages; i++) {
            let activeClass = (i === parseInt(this.props.currentPage, 10)) ? 'active' : '';
            pageLinks.push(<li key={i} className={activeClass}><a href="#" onClick={() => this.handleClick(i)}>{i}</a></li>);
        }

        return (
            <ul className="pagination">
                { parseInt(this.props.currentPage, 10) !== 1 ?
                    <li><a href="#" onClick={() => this.handleClick(parseInt(this.props.currentPage, 10) - 1)}><i className="fa fa-arrow-left"></i></a></li>
                    :
                    <li className="disabled"><a href="#"><i className="fa fa-arrow-left"></i></a></li>
                }
                {pageLinks}
                { parseInt(this.props.currentPage, 10) !== parseInt(this.props.numberOfPages, 10) ?
                    <li><a href="#" onClick={() => this.handleClick(parseInt(this.props.currentPage, 10) + 1)}><i className="fa fa-arrow-right"></i></a></li>
                    :
                    <li className="disabled"><a href="#"><i className="fa fa-arrow-right"></i></a></li> 
                }

            </ul>
        );
    }
}
*/
