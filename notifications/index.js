
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

const functions = require('firebase-functions');
const admin = require('firebase-admin')
admin.initializeApp(functions.config().firebase);


exports.notofications = functions.database.ref('/Notifications')
.onWrite(event => {
	//const key = event.params.Notifications;

	  const getDeviceTokensPromise = admin.database().ref('/user_keys').once('value');
	  //console.log('one');
	  return Promise.all([getDeviceTokensPromise]).then(results =>{

	  	const snap = results[0];
		//console.log('two');
	  	// if (!snap.hasChildren()) {
	  	// 	return console.log('no devices registered');
	  	// }

	  	//console.log('there are',snap.numChildren(),'devices to send notifications');

	  	const payload = {
	  	data :{

	  		title: "waah!",
	  		body: "It's Working!!"
	  	}
	  };

	  const tokens = Object.keys(snap.val());

	  return admin.messaging().sendToDevice(tokens,payload)
	  .then(function(response){
	  		console.log("Success", response);
	  	})
	  	.catch(function(error){
	  		console.log("Error", error);
	  	})
	});
});



