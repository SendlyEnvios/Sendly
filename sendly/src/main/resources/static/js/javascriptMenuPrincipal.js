const usuarioId = sessionStorage.getItem('usuarioId');//Recebe o Cookie
const name = document.querySelector('#name');
const endereco = document.querySelector('#endereco');
const iconPerfil = document.querySelector('#iconPerfil');
const entregasAtivas = document.querySelector('#entregasAtivas');
const statusPedido = document.querySelector('#statusPedido');
const statusTempo = document.querySelector('#statusTempo');
const statusDSM = document.querySelector('#statusDSM');
const statusEndereco = document.querySelector('#statusEndereco');
const statusEntregador = document.querySelector('#statusEntregador');

//Deslogando, será necessário carregar isto:
//sessionStorage.removeItem('usuarioId');
//window.location.href = './login.html';

//Apagar todos os cookies: sessionStorage.clear();

async function menuInfo(){ 
const usuarioId = sessionStorage.getItem('usuarioId');//Recebe o Cookie
let data = await fetch(
    `https://pwjob-production-1606.up.railway.app/cadastro?id=${usuarioId}`
);

data = await data.json();

name.innerHTML = data.name;
endereco.innerHTML = data.endereco;
entregasAtivas.innerHTML = data.entregasAtivas;
statusPedido.innerHTML = data.statusEntregaRecente;
statusTempo.innerHTML = data.estimativaER;
statusEndereco.innerHTML = 'Av.Paulo Guilguer Reimberg';
statusEntregador.innerHTML = 'Jorge Tomato Silva';
//iconPerfil?
};


setInterval(menuInfo(), 180000);