var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');

app.set('port', (process.env.PORT || 5000));

app.use(express.static(__dirname + '/client'));
app.use(bodyParser.json());

// Lot Model
Lot = require('./models/lot');

var uristring = process.env.MONGOLAB_URI || process.env.MONGOHQ_URL || 'mongodb://localhost:3000/';

// Connect to Mongoose

mongoose.connect(uristring, function (err, res) {
    if (err) {
        console.log ('ERROR connecting to: ' + uristring + '. ' + err);
    } else {
        console.log ('Succeeded connected to: ' + uristring);
    }
});

/*
 * LOT ENDPOINTS
 */

// Default
app.get('/', function (req, res) {
    res.sendfile("html/app.html");
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

// Get lot by id
app.get('/api/lots/:_id', (req, res) => {
    Lot.getLotById(req.params._id, (err, lot) => {
        if(err){
            console.log('status 404');
            res.status(404);
        }
        res.json(lot);
    });
});

// Add lot
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

// Update lot by id
app.put('/api/lots/:_id', (req, res) => {
    var id = req.params._id;
    var lot = req.body;
    Lot.updateLot(id, lot, {new: true}, (err, lot) => {
        if(err){
            console.log('status 404');
            res.status(404);
        }
        res.json(lot);
    });
});

// Delete lot by id
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

app.listen(app.get('port'), function() {
    console.log('Node app is running on port', app.get('port'));
});