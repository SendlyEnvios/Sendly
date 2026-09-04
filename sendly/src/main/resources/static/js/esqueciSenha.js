const form = document.querySelector('form');
const errorP = document.querySelector('#errorP');
form.addEventListener('submit', async (e) => {
    
    e.preventDefault();

    try{

    const dados = new FormData(form);

    await fetch('https://sendly-production-b679.up.railway.app/cadastroUpdate',{method: 'POST', body: dados});
        }catch(e){
            errorP.innerHTML = 'Gmail incorreto. Tente novamente'
            errorP.style.color = 'red'
        };
});