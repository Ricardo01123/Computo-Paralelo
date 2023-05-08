const{io} = require('../server');

io.on('connection', (client) => {
    console.log('Usuario conectado');

    // Enviar mensaje al cliente
    client.emit('enviarMensaje', {
        usuario: 'Administrador',  // usuario que envia el mensaje
        mensaje: 'Bienvenido a esta aplicacion'
    });

    client.on('disconnect', () => {
        console.log('Usuario desconectado');
    });

    // Escuchar el cliente
    client.on('enviarMensaje', (data, callback) => {
        console.log(data);

        // Enviar mensaje a todos los usuarios
        client.broadcast.emit('enviarMensaje', data); // emite a todos los usuarios
        //callback(); // ejecuta la funcion que se envia desde el cliente

     /*   if(mensaje.usuario){
            callback({
                resp: 'Todo salio bien'
            });
        }else{
            callback({
                resp: 'Todo salio mal'
            });
        }*/
    });
});