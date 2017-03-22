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
    var update = {
        name: lot.name,
        address: lot.address,
        lat: lot.lat,
        lng: lot.long,
        phoneNumber: lot.phoneNumber,
        capacity: lot.capacity,
        occupancy: lot.occupancy,
        price: lot.price,
        paymentType: lot.paymentType,
        hours: lot.hours,
        handicapParking: lot.handicapParking
    }
    Lot.findOneAndUpdate(query, update, options, callback);
}

// Delete Lot
module.exports.removeLot = (id, callback) => {
    var query = {_id: id};
    Lot.remove(query, callback);
}

