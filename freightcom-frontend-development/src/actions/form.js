
export const FORM_MULTIPLE_REMOVE_FIELDARRAY = 'redux-form/ARRAY_REMOVE_MULTIPLE';

export const removeMultipleFromArray = (form, field, indexes) => {

	return {
		type: FORM_MULTIPLE_REMOVE_FIELDARRAY,
		payload: {
			form,
			field,
			indexes
		}
	}
}
