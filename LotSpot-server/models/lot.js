const mongoose = require('mongoose');

// Lot Schema
const lotSchema = mongoose.Schema({
    name:{
        type: String,
        required: true
    },
    address:{
        type: String,
        required: true
    },
    lat:{
        type: String,
        default: ''
    },
    lng:{
        type: String,
        default: ''
    },
    phoneNumber:{
        type: String,
        default: ''
    },
    capacity:{
        type: Number,
        defualt: '0'
    },
    occupancy:{
        type: Number,
        default: '0'
    },
    price:{
        type: String,
        default: '0'
    },
    paymentType:{
        type: String,
        enum:['Hourly', 'Daily', 'Monthly', 'Yearly'],
        default: 'Hourly'
    },
    hours:{
        type: String,
        default: '00:00 - 23:59'
    },
    handicapParking:{
        type: Boolean,
        defualt: false
    }
});

const Lot = module.exports = mongoose.model('Lot', lotSchema);

// Get Lots
module.exports.getLots = (callback, limit) => {
    Lot.find(callback).limit(limit);
}

// Get Lot
module.exports.getLotById = (id, callback) => {
    Lot.findById(id, callback);
}

// Add Lot
module.exports.addLot = (lot, callback) => {
    Lot.create(lot, callback);
}

// Update Lot
module.exports.updateLot = (id, lot, options, callback) => {
    var query = {_id: id};
    var update = {}
    if (lot.name) {
        update.name = lot.name;
    }
    if (lot.address) {
        update.address = lot.address;
    }
    if (lot.lat) {
        update.lat = lot.lat;
    }
    if (lot.lng) {
        update.lng = lot.lng;
    }
    if (lot.phoneNumber) {
        update.phoneNumber = lot.phoneNumber;
    }
    if (lot.capacity) {
        update.capacity = lot.capacity;
    }
    if (lot.occupancy) {
        update.occupancy = lot.occupancy;
    }
    if (lot.price) {
        update.price = lot.price;
    }
    if (lot.paymentType) {
        update.paymentType = lot.paymentType;
    }
    if (lot.hours) {
        update.hours = lot.hours;
    }
    if (lot.handicapParking) {
        update.handicapParking = lot.handicapParking;
    }
    Lot.findOneAndUpdate(query, {$set: update}, options, callback);
}

// Delete Lot
module.exports.removeLot = (id, callback) => {
    var query = {_id: id};
    Lot.remove(query, callback);
}

