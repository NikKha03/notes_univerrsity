import React, { Component } from 'react';

import UserService from '../services/User';
import EventBus from '../common/EventBus';

export default class BoardUser extends Component {
	constructor(props) {
		super(props);

		this.state = {
			content: '',
		};
	}

	componentDidMount() {
		UserService.getUserBoard().then(
			response => {
				this.setState({
					content: this.displayValues(response.data.notes),
				});
				console.log(response.data.notes);
			},
			error => {
				this.setState({
					content: (error.response && error.response.data && error.response.data.message) || error.message || error.toString(),
				});

				if (error.response && error.response.status === 401) {
					EventBus.dispatch('logout');
				}
			}
		);
	}

	render() {
		return <div className='container res'>{this.state.content}</div>;
		// return this.displayValues(this.state.content);
	}

	displayValues(arr) {
		console.log(arr);
		arr.forEach(obj => {
			const header = obj.header;
			const text = obj.text;

			const h1Element = document.createElement('h3');
			h1Element.textContent = header;
			document.querySelector('.container').appendChild(h1Element);

			const aElement = document.createElement('p');
			aElement.textContent = text;
			document.querySelector('.container').appendChild(aElement);
		});
	}
}
