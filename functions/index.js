// Import the Firebase SDK for Google Cloud Functions.
const functions = require('firebase-functions');
// Import and initialize the Firebase Admin SDK.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);



// Sends a notifications to all users when a new message is posted.
exports.sendNotifications = functions.database.ref('/testNotif').onWrite(event => {
  const snapshot = event.data;
  // // Only send a notification when a new message has been created.
  // if (snapshot.previous.val()) {
  //   return;
  // }

  // Notification details.
  // const text = snapshot.val().text;
  // const payload = {
  //   notification: {
  //     title: `${snapshot.val().name} posted ${text ? 'a message' : 'an image'}`,
  //     body: text ? (text.length <= 100 ? text : text.substring(0, 97) + '...') : '',
  //     icon: snapshot.val().photoUrl || '/images/profile_placeholder.png',
  //     click_action: `https://${functions.config().firebase.authDomain}`
  //   }
  // };


  // The topic name can be optionally prefixed with "/topics/".
  var topic = "/topics/food";

  // See the "Defining the message payload" section below for details
  // on how to define a message payload.
  var payload = {
    data: {
      score: "850",
      time: "2:45"
    }
  };

  // Send a message to devices subscribed to the provided topic.
  return admin.messaging().sendToTopic(topic, payload)
    .then(function(response) {
      // See the MessagingTopicResponse reference documentation for the
      // contents of response.
      console.log("Successfully sent message:", response);
    })
    .catch(function(error) {
      console.log("Error sending message:", error);
    });




  // Get the list of device tokens.
  // return admin.database().ref('fcmTokens').once('value').then(allTokens => {
  //   if (allTokens.val()) {
  //     // Listing all tokens.
  //     const tokens = Object.keys(allTokens.val());
  //
  //     // Send notifications to all tokens.
  //     return admin.messaging().sendToDevice(tokens, payload).then(response => {
  //       // For each message check if there was an error.
  //       const tokensToRemove = [];
  //       response.results.forEach((result, index) => {
  //         const error = result.error;
  //         if (error) {
  //           console.error('Failure sending notification to', tokens[index], error);
  //           // Cleanup the tokens who are not registered anymore.
  //           if (error.code === 'messaging/invalid-registration-token' ||
  //               error.code === 'messaging/registration-token-not-registered') {
  //             tokensToRemove.push(allTokens.ref.child(tokens[index]).remove());
  //           }
  //         }
  //       });
  //       return Promise.all(tokensToRemove);
  //     });
  //   }
  // });


});
