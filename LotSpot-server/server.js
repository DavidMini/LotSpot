var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');

app.use(express.static(__dirname+'/client'));
app.use(bodyParser.json());

// Lot Model
Lot = require('./models/lot');

// Connect to Mongoose
mongoose.connect('mongodb://localhost/LotSpot');
var db = mongoose.connection;


/*
* LOT ENDPOINTS
*/

// Default
app.get('/', (req, res) => {
	res.send('Please use /api/lots');
});

// Get all lots
app.get('/api/lots', (req, res) => {
    Lot.getLots((err, lots) => {
        if(err){
            console.log('status 404');
            res.status(404);
        }
        res.json(lots);
    });
});

app.get('/api/lots/:_id', (req, res) => {
    Lot.getLotById(req.params._id, (err, lot) => {
        if(err){
            console.log('status 404');
            res.status(404);
        }
        res.json(lot);
    });
});

app.post('/api/lots', (req, res) => {
    var lot = req.body;
    Lot.addLot(lot, (err, lot) => {
        if(err){
            console.log('status 404');
            res.status(404);
        }
        res.json(lot);
    });
});

app.put('/api/lots/:_id', (req, res) => {
    var id = req.params._id;
    var lot = req.body;
    Lot.updateLot(id, lot, {}, (err, lot) => {
        if(err){
            console.log('status 404');
            res.status(404);
        }
        res.json(lot);
    });
});

app.delete('/api/lots/:_id', (req, res) => {
    var id = req.params._id;
    Lot.removeLot(id, (err, lot) => {
        if(err){
            console.log('status 404');
            res.status(404);
        }
        res.json(lot);
    });
});

// Where to connect
app.listen(3000);
console.log('Running on port 3000...');
