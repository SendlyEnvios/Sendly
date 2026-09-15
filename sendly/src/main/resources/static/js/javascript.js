const form = document.querySelector('form');
const p = document.querySelectorAll('p');
const inputs = document.querySelectorAll('input');
sessionStorage.clear();

form.addEventListener('submit', async (e) => {
    
    e.preventDefault();

    const dados = new FormData(form);

    let data = await fetch('https://sendly-production-b679.up.railway.app/cadastro',{method: 'POST', body: dados});
    
    data = await data.json();

    
    if(data.id){
        sessionStorage.setItem('usuarioId', data.id);
        window.location.href = './menuprincipal.html'
    }else{
        p.forEach(noLogin => {
            noLogin.innerHTML = data.mensagem;
            noLogin.style.color = "red";
        });
    };
});

inputs.forEach(input => {
    input.addEventListener('input', ()=> {
        p.forEach(noLogin =>{
            noLogin.innerHTML = '';
        });
    });
});