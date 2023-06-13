$(function () {

    const socket = io();

    // Obtener los elementos del DOM desde la interfaz
    const $messageForm = $('#message-form');
    const $messageBox = $('#message');
    const $chat = $('#chat');

    // Eventos
    $messageForm.submit(e => {
        e.preventDefault();
        socket.emit('send message', $messageBox.val());
        $messageBox.val('');
    });

})

