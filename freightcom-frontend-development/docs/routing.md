# Routing

The app uses `react-router` to handle routing. Currently using **v3**. React Router v4 has breaking changes and it is incompatible with the existing implementation.

## Creating new route

Create a _Page_ Component inside `src/pages/`.

```Javascript
class SomePage extends Component {
  render() {
    return (
      <Widget title="Title of the Page">
        //...content goes here
      </Widget>
    );
  }
}
```

Edit `src/routes.js` file. Import the page component and add a Route definition
```Javascript
//...other imports
import SomePage from '../pages/SomePage';

//...other route declaration
<Route path="/some-page" component={SomePage} />
```

If you need to add access restriction to the route, add an `onEnter` props
```Javascript
<Route path="/some-page" component={SomePage} onEnter={checkAccess(['ADMIN'])} />
```

## Passing and accessing URL Parameter
```Javascript
<Route path="/some-page/:someId" component={SomePage} />

// -------------
// pages/SomePage.js
render() {
  const pageTitle = 'Some - ' + this.props.routeParams.someId;
  return (
      <Widget title={pageTitle}>
        //...content
      </Widget>
    )
}
```

## Adding to the sidebar Navigation

Edit `src/components/Navigation.js` file. Add NavItem component.
Only those roles specified in `authorizedRole` will the menu show up.

```Javascript
<NavItem
  activeRole={activeRole.roleName}
  authorizedRole={['CUSTOMER_ADMIN', 'CUSTOMER_STAFF']}
  url="/some-page"
>
  <Link to="/some-page">
    <i className="fa fa-lg fa-fw fa-file" />
    <span className="menu-item-parent"> Some Page</span>
  </Link>
</NavItem>
```
