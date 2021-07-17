'use strict';

// Import Express
var express = require('express');
var User = require('../models/user');

// Import .env
const dotenv = require('dotenv');
dotenv.config();

// Import Firebase Admin Account
var admin = require("firebase-admin");

// Create a router
var router = express.Router();

// Configure Firebase Admin Account
if (!admin.apps.length){
    admin.initializeApp({
        credential: admin.credential.cert({
            projectId: process.env.FIREBASE_PROJECT_ID,
            clientEmail: process.env.FIREBASE_CLIENT_EMAIL,
            privateKey: process.env.FIREBASE_PRIVATE_KEY
        })
    });
} else {
    admin.app();
}

// API ROUTES

// GET - fetches ALL users
router.get('/users', function(request, response){
    User.find({}, function(err, users) {
        if (err) {
            return response.status(500).json({message: err.message});
        }

        response.json({users: users});

    });
});

// GET - fetches user by Firebase ID Token
router.get('/users/:idToken', function(request, response){
    var idToken = request.params.idToken

    admin.auth().verifyIdToken(idToken).then((decodeToken) => {
        const uid = decodeToken.uid
        var query = {'firebaseUid': uid};
        
        User.findOne(query, function(err, userFound) {
            if (err) {
                return response.status(500).json({err, userFound});
            }
    
            if (!userFound){
                return response.status(500).json({err: "The user does not exist in the database."});
            }
            response.json({"user": userFound});
        });
    })
    .catch((error) => {
        return response.status(500).json({err: "Invalid Firebase ID token."})
    })

})

// POST - adds user to the database
router.post('/users/:idToken', function(request, response){
    var user = request.body;
    var email = user.email;
    var query = {'email': email};
    var idToken = request.params.idToken

    User.findOne(query, function(err, userNew) {
        if (err) {
            return response.status(500).json({err, userNew});
        }

        if (userNew){
            return response.status(500).json({err: "The user already exists in the database."});
        }

        else {

            admin.auth().verifyIdToken(idToken).then((decodeToken) => {
                const uid = decodeToken.uid

                user['firebaseUid'] = uid;

                User.create(user, function(err, user){
                    if (err) {
                        return response.status(500).json({err, user});
                    }
            
                    response.json({'user': user, message: 'User created.'});
                });
            })
            .catch((error) => {
                return response.status(500).json({err: "Invalid Firebase ID token."})
            })
        }
    });
});

// PUT - updates an existing user in the database
router.put('/users/:idToken', function(request, response){
    var idToken = request.params.idToken;
    var user = request.body;

    admin.auth().verifyIdToken(idToken).then((decodeToken) => {
        const uid = decodeToken.uid
        var query = {'firebaseUid': uid};
        
        User.findOne(query, function(err, userFound) {
            if (err) {
                return response.status(500).json({err, userFound});
            }
    
            if (!userFound){
                return response.status(500).json({err: "The user does not exist in the database."});
            }
            
            user['firebaseUid'] = uid;
            
            User.findOneAndUpdate(query, user, {new: true}, (err, user) => {
                if(err) {
                  return response.status(500).json({err: err.message});
                }
            
                response.json({'user': user, message: 'User updated.'});
            });
        });
    })
    .catch((error) => {
        return response.status(500).json({err: "Invalid Firebase ID token."})
    })
});

// DELETE - deletes existing user from database
router.delete('/users/:idToken', function(request, response){
    var idToken = request.params.idToken;

    admin.auth().verifyIdToken(idToken).then((decodeToken) => {
        const uid = decodeToken.uid
        var query = {'firebaseUid': uid};
        
        User.findOne(query, function(err, userFound) {
            if (err) {
                return response.status(500).json({err, userFound});
            }
    
            if (!userFound){
                return response.status(500).json({err: "The user does not exist in the database."});
            }

            User.findOneAndDelete(query, function(err, user){
                if(err) {
                    return response.status(500).json({err: err.message});
                }
    
                response.json({'user': user, message: 'User deleted.'});
            });
        });
    })
    .catch((error) => {
        return response.status(500).json({err: "Invalid Firebase ID token."})
    })
});

module.exports = router;