import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { connect } from 'react-redux';
import { saveOrderLog } from '../../actions/orders';
import { addNotification } from 'reapop';

class AddCommentModal extends Component {
  render() {
    const { show, toggleAddCommentModal } = this.props;
    return (
      <div>
        <Modal show={show} onHide={toggleAddCommentModal}>
          <Modal.Header closeButton>
            <Modal.Title>Add Comment</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form onSubmit={this.submit} className="modalForm form-horizontal">
              <div className="row">
                <div className="col-sm-12 col-xs-12">
                  <div className="form-group">
                    <label>Comment:</label>
                    <textarea
                      className="form-control"
                      rows="5"
                      name="comment"
                      ref="comment"
                    />
                  </div>
                </div>
              </div>
              <div className="row form-group">
                <div className="col-sm-12 col-xs-12">
                  <div className="checkbox">
                    <label>
                      <input
                        type="checkbox"
                        name="messageType"
                        value="invoice"
                        ref="messageType"
                      /> {'  '}
                      Invoice
                    </label>
                  </div>
                  <div className="checkbox">
                    <label>
                      <input
                        type="checkbox"
                        name="private"
                        value={1}
                        ref="private"
                      /> {'  '}
                      Private
                    </label>
                  </div>
                </div>
              </div>
              <br />
              <div className="row modalForm-footer">
                <div className="col-sm-12 col-xs-12">
                  <Button
                    onClick={this.props.toggleAddCommentModal}
                    className="btn"
                  >
                    Cancel
                  </Button>
                  <Button type="submit" className="btn btn-primary">
                    Save
                  </Button>
                </div>
              </div>
            </form>
          </Modal.Body>
        </Modal>
      </div>
    );
  }
  submit = e => {
    e.preventDefault();
    const comment = {
      userId: this.props.loggedInUserId,
      franchiseId: 1,
      messageType: this.refs.messageType.checked
        ? this.refs.messageType.value
        : 'order',
      comment: this.refs.comment.value,
      private: this.refs.private.checked ? 1 : 0,
    };
    // console.log(comment);
    this.props.saveOrderLog(this.props.order.id, comment, data => {
      this.props.addNotification({
        title: 'New comment',
        message: `New comment has been added to the order, ${this.props.order.id}`,
        status: 'success',
        dismissible: false,
        dismissAfter: 5000,
      });
      this.props.loadOrderLogs(this.props.order.id);
      this.props.toggleAddCommentModal();
    });
  };
}

const mapStateToProps = state => {
  const { loggedInUser } = state;
  return { loggedInUserId: loggedInUser.id };
};

const mapDispatchToProps = { saveOrderLog, addNotification };

export default connect(mapStateToProps, mapDispatchToProps)(AddCommentModal);
