module.exports = function(io) {
    io.on('connection', socket => {
        console.log('New user connected');
    });
}