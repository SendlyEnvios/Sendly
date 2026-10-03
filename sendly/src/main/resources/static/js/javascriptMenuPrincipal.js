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

const main = document.querySelector('main');
const buttonsRealizarEntrega = document.querySelector("#realizarEntrega");
const buttonsRastrearPedido = document.querySelector("#rastrearPedido");
const buttonsTrabalheConosco = document.querySelector("#trabalheConosco");

//Deslogando, será necessário carregar isto:
//sessionStorage.removeItem('usuarioId');
//window.location.href = './login.html';

//Apagar todos os cookies: sessionStorage.clear();

async function menuInfo(){ 
const usuarioId = sessionStorage.getItem('usuarioId');//Recebe o Cookie]
let data = await fetch(
    `https://sendly-production-b679.up.railway.app/cadastro?id=${usuarioId}`
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

buttonsRealizarEntrega.addEventListener('Click', ()=>{
    main.innerHTML = `
        <section id="paragrafoHero">
            <div>
                <h1>Sendly</h1>
                <p>Somos uma séria empresa de negócios, onde você e o pacote</p>
                <p> são a prioridade.</p>
            </div>
        </section>
        <section id="imagemHero">
            <img src="imgs/icon_box.png" alt="boxHero">
        </section>
        <section id="buttonsHero">
            <button id="realizarEntrega">Realizar Entrega</button>
            <button id="rastrearPedido">Rastrear Pedido</button>
            <button id="trabalheConosco">Trabalhe W</button>
        </section>
    `;
})
/* buttonsRastrearPedido.
buttonsTravalheConosco. */

setInterval(menuInfo(), 180000);