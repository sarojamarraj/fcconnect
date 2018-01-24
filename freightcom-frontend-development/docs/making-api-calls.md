# Making API Calls

## How to
### Add action creator
```Javascript
import { getAll, getOne, save, update } from './actions/api';
import { SomeSchema } from '../schemas';

export const someAction = (someId, callback) => dispatch => {
  dispatch(getAll('someEntity', SomeSchema)).then(callback);
}
```

### Connect React components
```Javascript
import { connect } from 'react-redux';
import { someAction } from '../actions/someActions';

class SomeComponent extends Component {
  ...
  render() {
    return <div>
      <button
        onClick={e => this.props.someAction('id', ({err = false}) => {
          if (err) {
            ...show error error notification
          } else {
            ...show success notification
          }
        }}
      >
        Call an API
      </button>
    </div>
  }
}

const mapDispatchToProps = { someAction };

export default connect(null, mapDispatchToProps)(SomeComponent);
```

## General Purpose API Action creators
Located in `src/actions/api.js`

### `getAll`
Used for getting a data list from the server to be displayed in table view. Response payload will be normalized and get merged into Redux store `entities.{entity parameter}` and in `pagination._{entity parameter}`.

#### `getAll` Parameters
| parameter | type | description |
|-|-|-|
| `entity` | String | Entity name defined in Schema
| `schema` | Object | Schema object created using `normalizr`
| `pageToLoad` | Number | Page to load for pagination
| `itemsPerPage` | Number | Size of the result
| `filters` | Object | Key-Value pair for filtering
| `sortOrder` | Object | Defaults to `{ sortBy: 'id', orderBy: 'DESC' }`
| `endpoint` | String | For overriding the endpoint. No need to add `/api/` at the beginning.
| `entityMap` | String | Use this if the name of the JSON response object is different from the `entity` name
| `redirectPath` | String | Subject for simplification.

#### Displaying results in `<DataTable>`
You can easily display results of `getAll` on a `<DataTable>` component using a selector `getPageItems`

#### `getAll` Usage
```Javascript
export const loadSubmittedOrders = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', orderBy: 'DESC' },
  callback,
) => dispatch => {
  dispatch(
    getAll(
      'order',
      OrderSchema,
      pageToLoad,
      itemsPer,
      filters,
      sortOrder,
      'submitted-order',
      'customerOrder'
    )
  ).then(callback);
}
```

### `getOne`
Used for getting a single entity from the server.

#### `getOne` Parameters
| parameter | type | description |
|-|-|-|
| `entity` | String | Entity name defined in Schema
| `id` | String/Number | Id of the entity
| `entityMap` | String | Use this if the name of the JSON response object is different from the `entity` name
| `endpoint` | String | For overriding the endpoint. No need to add `/api/` at the beginning.
| `callbackAction` | Function | Function to be called when request is done. **Deprecated!** Use `dispatch(getOne(...)).then(callback)`
| `redirectPath` | String | Subject for simplification

#### `getOne` Usage
```Javascript
export const loadOrderById = (orderId, callback) => dispatch => {
  dispatch(getOne('order', orderId)).then(callback);
}
```

### `save`

#### `save` Parameters
| parameter | type | description |
|-|-|-|
| `entity` | String | Entity name defined in Schema
| `data` | Object | Id of the entity
| `endpoint` | String | For overriding the endpoint. No need to add `/api/` at the beginning.
| `key` | String |
| `method` | String | `POST` or `PUT`
| `callbackAction` | Function | Function to be called when request is done. **Deprecated!** Use `dispatch(save(...)).then(callback)`
| `redirectPath` | String | Subject for simplification

#### `save` Usage
```Javascript
export const saveNewOrder = (orderData, callback) => dispatch => {
  dispatch(save('order', orderData)).then(callback);
}
```

### `update`

#### `update` Parameters
| parameter | type | description |
|-|-|-|
| `entity` | String | Entity name defined in Schema
| `id` | String/Number | Id of the entity
| `data` | Object | Id of the entity
| `endpoint` | String | For overriding the endpoint. No need to add `/api/` at the beginning.
| `key` | String |
| `method` | String | `POST` or `PUT`
| `callbackAction` | Function | Function to be called when request is done. **Deprecated!** Use `dispatch(update(...)).then(callback)`
| `redirectPath` | String | Subject for simplification

#### `update` Usage
```Javascript
export const updateOrder = (orderId, orderData, callback) => dispatch => {
  dispatch(save('order', orderId, orderData)).then(callback);
}
```

### Example API Callback Function
It's a common pattern to pass a callback function whenever you do a server request. You can do so by adding your callback to the `dispatch(save(...)).then(callback)` method.

```Javascript
const callback = (apiActionObject) => {
  if (apiActionObject.error) {
    //Assumption: this snippet is inside a React component
    this.props.addNotification({
      message: 'There was an error in your request',
      status: 'error'
    });
    //... do some more in case of error
  } else {
    this.props.addNotification({
      message: 'Request success...',
      status: 'success'
    });
    //... do some more in case of error
    console.log(apiActionObject.payload)
  }
}
```

## Custom Request

```Javascript
export const someActionCreator = () => dispatch => {

  fetch('/api-endpoint', { credentials: 'include', method: 'GET'})
    .then(response => response.json())
    .then(json => {
      console.log(json);
      dispatch({
        type: 'SOME_REQUEST_SUCCESS',
        payload: json,
      })
    })
    .catch(error => {
      console.error(error)
      dispatch({
        type: 'SOME_REQUEST_ERROR',
        error: true,
        payload: error,
      });
    });
}
```
