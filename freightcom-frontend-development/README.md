# Freightcom Frontend
Frontend for **Freightcom**. It's a single page application built using React and Redux. It connects to Frightcom Server via REST API.

## Getting started

### Prerequisites
- Node.js and NPM (_for development only_)
- [Freightcom API](https://github.com/palominoinc/freightcom-api) Server running

### Developing

```shell
$ git clone https://github.com/palominoinc/freightcom-frontend.git
$ cd freightcom-frontend
$ npm install
$ npm start
```
This will open a Chrome tab and point to `http://localhost:3000`. Any changes to files inside `src` will trigger a reload of the webpage.

### Building for Production
```shell
$ cd freightcom-frontend
$ npm run build
```
Running build will create a `build` directory that contains the static html/css/js file that can be deployed to a web server. It expects the API server to be on the same webroot accessible via `/api/` prefix.

## Documentation
- [Development](./docs/development.md)
- [Architecture](./docs/architecture.md)
- [Managing App State](./docs/managing-app-state.md)
- [Making API Calls](./docs/making-api-calls.md)
- [Styling](./docs/styling.md)
- [Routing](./docs/routing.md)
- [React Components](./docs/react-components.md)
