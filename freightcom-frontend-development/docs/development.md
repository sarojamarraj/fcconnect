# Development
## Getting Started
### Prerequisites
- Node.js and NPM
- Freightcom API Server running

### Developing

```shell
$ git clone https://github.com/palominoinc/freightcom-frontend.git
$ cd freightcom-frontend
$ npm install
$ npm start
```
This will open a Chrome tab and point to `http://localhost:3000`. Any changes to files inside `src` will trigger a reload of the webpage.

### Configure Server API Endpoint
To point the app to the API server, edit the `proxy` attribute on `package.json`. This is only for development to proxy server request for CORS workaround.

## Setting up Development tools
- Install [React Devtools Chrome extension](https://chrome.google.com/webstore/detail/react-developer-tools/fmkadmapgofadopljbjfkapdkoienihi?hl=en). Load Chrome inspector and select **React** tab.
- Install [Redux DevTools Chrome extension](https://chrome.google.com/webstore/detail/redux-devtools/lmhkpmbekcpmknklioeibfkpmmfibljd).
Load Chrome inspector and select **Redux** tab.
- Install [Prettier](https://github.com/prettier/prettier) integration. Check `prettier` docs for specific IDE integration (Atom, Emac, Sublime, VSCode, Vim, etc.).

### Coding practices
- Use of **ES6+** features. Only use features that are at least **Stage 3** proposals. _(No use of Decorators or Observables for now)_
- Use [`prettier`](https://github.com/prettier/prettier) for formatting code. `--single-quotes --trailing-comma all`
- Remove console logs/debugs before pushing to development
- Cleanup lint errors/warnings before pushing to development
