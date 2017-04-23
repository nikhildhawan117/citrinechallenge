const express = require('express');
const morgan = require('morgan');

const app = express();

app.use(morgan('dev'));

app.get('/units/si', function (req, res) {
  //little bit of coding magic
  const json = {};
  const exec = require('child_process').exec;
  const child = exec('java -jar ./SIConverter.jar ' + '"' + req.query.units + '"',
  function (error, stdout, stderr){
    console.log('QUERY:', req.query.units);
    json['unit_name'] = stdout.split(' ')[0];
    json['multiplication_factor'] = stdout.split(' ')[1];
    console.log('STDOUT:', stdout);
    if (stdout.length === 0) {
      res.status(400).send('Invalid input');
    } else if (error !== null){
      res.status(500).send(error);
    } else {
      res.json(json);
    }
  });
});

app.get('*', function(req, res) {
  res.sendStatus(404);
})

app.set('port', 8000);

app.listen(app.get('port'));
console.log('Running on port ' + app.get('port'));