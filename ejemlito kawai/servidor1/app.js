const express = require('express');
const app = express();

app.get('/index', (req, res) => {
    console.log('alguien entro');
    res.send('Hello World!');
    }
);

app.listen(8080, () => {
    console.log('Example app listening on port 3000!');
    });