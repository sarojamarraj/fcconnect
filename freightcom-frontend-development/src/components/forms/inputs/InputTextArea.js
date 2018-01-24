import React from 'react';
import { Field } from 'redux-form';

/*
const InputText = (props) => {
    return <div className="wo-form-group">
        <label className="wo-label">{props.label}</label>
        <textarea className="wo-input" rows="5" {...props} />
    </div>
}
*/

const InputTextArea = function InputTextArea(props) {
	const {
		label,
		name,
    error = false,
		note = false
	} = props;
	return (
		<section>
			<label className="label">{label}</label>
			<label className={error ? 'textarea state-error' : 'textarea'}>
				<Field
					name={name}
					component="textarea"
					className="custom-scroll"
				/>
			</label>
			{ note ?
				<div className="note">
					{note}
				</div> : ''
			}
		</section>
	);
}

export default InputTextArea;
