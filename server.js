const express = require('express');
const morgan = require('morgan');

const app = express();

app.use(morgan('dev'));

app.get('/units/si', function (req, res) {
  const json = {};
  const exec = require('child_process').exec;

  //to prevent problems with running args in bash
  const query2 = (req.query.units).replace(/°/g, "degree");
  const query3 = query2.replace(/"/g, "\\" + "\"");
  const child = exec('java -jar ./SIConverter1.7.jar ' + '"' + query3 + '"',
  function (error, stdout, stderr){
    json['unit_name'] = stdout.split(' ')[0];
    json['multiplication_factor'] = parseFloat(stdout.split(' ')[1]);
    if (stdout.length === 0) {
      res.status(400).send('Invalid input');
    } 
    else if (error !== null){
      res.status(500).send(error);
    } 
    else {
      res.json(json);
    }
  });
});

app.get('*', function(req, res) {
  res.sendStatus(404);
})

app.set('port', (process.env.PORT || 8000));

app.listen(app.get('port'));
console.log('Running on port ' + app.get('port'));