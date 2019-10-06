const functions = require('firebase-functions');
const admin =require('firebase-admin');
admin.initializeApp()

exports.sendNotification = functions.firestore.document('Notebook/{id}').onCreate((snap,context)=>{
    const root=snap.data();

    const message=root.description;
    const image=root.image;
    const name=root.name;
    console.log("name:"+name+"  message:"+message)

    const payload={
        data:{
            Sender:name,
            Image:image,
            Message:message
        }
    }

    return admin.messaging().sendToTopic("Notification",payload)
    .catch(error=>{
        console.log(error)
    })

});