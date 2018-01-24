# React Components


## `<Widget>`
Container for components in `/pages/`.

### Props

| props | type | description
|-|-|-
| `title` | String | Title of the pages

### Usage
```Javascript
render() {
  return (
    <Widget title="Orders">
      ...
    </Widget>
  );
}
```

## `<FormGroup>`
Component to create bootstrap input form group.

### Props

| props | type | description
|-|-|-
| `label` | String | Text for the `<label>` element
| `type` | String | Text for `<input type="..." />`
| `inputRef` | Function | Get the reference of the input element
| _any_ | ... | Additional props will be passed to the `<input>` element
| `children` | element | If `type` props is not set, it will expect a `children` element

### Usage
```Javascript
render() {
  return (
    // With type props
    <FormGroup label="First Name" type="text" onChange={...} />

    // Without type props
    <FormGroup label="Status">
      <select className="form-control">
        <option>Active</option>
        <option>Inactive</option>
    </FormGroup>
  )
}
```
HTML Output:
```HTML
<!-- With type props -->
<div className="form-group">
  <label className="form-label">FirstName</label>
  <input className="form-control" type="text" onChange={...} />
</div>

<!-- Without type props -->
<div className="form-group">
  <label className="form-label">FirstName</label>
  <select className="form-control">
    <option>Active</option>
    <option>Inactive</option>
  </select>
</div>

```

## `<Icon>`
Component to display an Icon in FontAwesome.

### Props

| props | type | description
|-|-|-
| `name` | String | Name of the icon to display

### Usage
```Javascript
render() {
  return (
    <Icon name="file" />
  );
}
```

## `<DataTable>`
Components for creating tabular data. It includes built-in pagination, filters and sorting

### Props

| props | type | description
|-|-|-
| `columns` | Array | **Required.** Array of object that represents the column.
| `loadData` | Function | **Required.** Function to call whenever datatable needs to reload the data. Datatable calls this function and pass the following parameters `loadData(pageToLoad, itemsPerPage, filters, sortOrder)`
| `items` | Array | **Required.** Array of object with the data
| `currentPage` | Number | **Required.** Current page
| `numberOfPages` | Number | **Required.** Pagination size
| `isFetching` | Boolean | **Required.** Whether to show loading indicator
| `groupedFilters` | Boolean | Default: false. If set to true, it will group all filters together at the top of the table
| `renderTableActionButtons` | Function | For customizing the `<thead>` for adding additional buttons
| `renderPagination` | Boolean | Default: true. If set to false, it will hide pagination buttons
| `renderTableFilters` | Boolean / Function | Use for customising column filters. This should return a `<tr>` element.
| `renderTableHeaders` | Boolean / Function | Use for customising column headers. This should return a `<tr>` element.


### Props - `columns`
`columns` props is an array of object that will determine how a column will render.

#### `columns` object keys

| key | type | description
|-|-|-
| `label` | String | Column header to display.
| `field` | String | Field name of the data. Used for displaying the content of the cell and also use for filtering and sorting the column
| `cell` | String / Function | If string, it will display the static string as cell content. If function, it accepts an object parameter that represents the data cell and displays whatever it returns (_String or React element_).
| `filter` | Boolean / Function | Default: true. By default filtering will use the `field` attribute to add filter using a text field. If set to `false`, it will not render a filter input for this column. If function, it accepts a function that will trigger adding a filter. `f => f({ field: 'value' })`
| `sorting` | Boolean | Default: true. If set to false it will disable sorting of column
| `className` | String | Class to add to `<th>` and `<td>`
| `width` | String / Number | Number in `px`. It adds `style={{width: value}}` to `<th>`


### Usage
```Javascript
constructor(props) {
  super(props);

  this.columns = [
    {
      label: 'ID',
      field: 'id',
    },
    {
      label: 'Status',
      field: 'status',
      filter: filter => <select onChange={e => filter({ status: e.target.value })}>
        <option>Active</option>
        <option>Inactive</option>
      </select>
    },
    {
      label: 'Some Field',
      filter: false,
      sorting: false,
    }
  ];
}

render() {
  return (
    <DataTable
      columns={this.columns}
      loadData={this.props.loadSomeData}
      {...this.props.datatableData}
    />
  );
}

const mapStateToProps = state => {
  return {
    datatableData: getPageItems(state, 'someEntity')
  }
}

```
