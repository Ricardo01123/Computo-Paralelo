const http = require('http');
const path = require('path');

const express = require('express');
const { Server } = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = new Server(server);

// Settings
app.set('port', process.env.PORT || 3000);

require('./sockets')(io);

// Static files
app.use(express.static(path.join(__dirname, 'public')));


// Start the server
server.listen(app.get('port'), () => {
    console.log('Server is running on port 3000', app.get('port'));
});
