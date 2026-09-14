const email = sessionStorage.getItem("email");
const form = document.querySelector('form');
const errorP = document.querySelector('#errorP');
const reenviarToken = document.querySelector('#a_criarUmaConta');

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        const dados = new FormData(form);
        const token = dados.get('token');

        const data = await fetch('https://sendly-production-b679.up.railway.app/cadastroUpdate2?token=' + token);
        if (data.ok) {
            sessionStorage.setItem("token", token);
            window.location.href = "./trocarSenha.html";
        }else{
            errorP.innerHTML = 'Token incorreto. Tente novamente'
            errorP.style.color = 'red'
        };
    } catch (e) {
        errorP.innerHTML = 'Ocorreu um erro. Tente novamente'
        errorP.style.color = 'red'
    };
});

reenviarToken.addEventListener('click', async (e) => {
    const dados = new FormData();
    dados.append('email', email);
    await fetch('https://sendly-production-b679.up.railway.app/cadastroUpdate',{method: 'POST', body: dados});
});