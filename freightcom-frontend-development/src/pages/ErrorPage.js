import React, { Component } from 'react';
import Widget from '../components/Widget';

class ErrorPage extends Component {
	render() {
		return (
			<Widget title="Error">
				<div id="errorPage">
					<h2>
						<i className="fa fa-warning"></i>
						{' '} 404 Page Not Found
					</h2>
					<p>
						No matching route or page found in this application. Check the URL or report a broken link.
					</p>
				</div>
			</Widget>
		);
	}
}

export default ErrorPage;
