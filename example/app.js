// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


var senderId = 'john.doe@gmail.com'; // You'll need to put in your own account here

var c2dm = require('com.findlaw.titanium.c2dm');
Ti.API.info("module is => " + c2dm);

c2dm.addEventListener('register', function(data) {
	Ti.API.info('JS register event: ' + data.registrationId);
	label.text = 'Registered: ' + data.registrationId;
});
c2dm.addEventListener('message', function(data) {
	Ti.API.info('JS message event: ' + data.message);
	label.text = 'Message: ' + data.message;
});


// open a single window
var window = Ti.UI.createWindow({
	backgroundColor:'white'
});

var label = Ti.UI.createLabel({top: 20, left: 5, right: 5});
window.add(label);

var button = Ti.UI.createButton({title: 'Register'});
button.addEventListener('click', function() {
	Ti.API.info('Registering...');
	label.text = 'Registering...';
	c2dm.register(senderId);
});
window.add(button);

window.open();


if(!c2dm.registrationId) {
	Ti.API.info('Registering...');
	label.text = 'Registering...';
	c2dm.register(senderId);
} else {
	Ti.API.info('Current registration id: ' + c2dm.registrationId);
	label.text = 'Current registration id: ' + c2dm.registrationId;
}