var newman = require('newman');

newman.run({
  collection: require('./freightcom-tests/postman-collection.json'),
  environment: require('./freightcom-tests/postman-environment.json'),
  reporters: ['html','cli'],
} , function (err) {
  if (err) { throw err; }
  console.log ('tests completed!'); 
});

