const resposta = await fetch('https://sendly-production-b679.up.railway.app/validarToken?token=' + token);

const resultado = await resposta.text();

console.log(resultado);