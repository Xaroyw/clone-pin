'use strict';

var mongoose = require('mongoose');

// Create the User schema

var userSchema = new mongoose.Schema({
    email: String,
    username: String,
    name: String,
    profilePicPathFromUri: String,
    bio: String,
    numFollowers: Number,
    numFollowing: Number,
    firebaseUid: String
});

// Build model
var model = mongoose.model('User', userSchema);

// Export model
module.exports = model;