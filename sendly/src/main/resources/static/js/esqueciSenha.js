const form = document.querySelector('form');
const errorP = document.querySelector('#errorP');
form.addEventListener('submit', async (e) => {
    e.preventDefault();

    try{
    const dados = new FormData(form);
    const email = dados.get('email');
    
    const data = await fetch('https://sendly-production-b679.up.railway.app/cadastroUpdate',{method: 'POST', body: dados});
        
    if(await data.text() === "Gmail inválido"){
        errorP.innerHTML = 'Gmail incorreto. Tente novamente'
        errorP.style.color = 'red'
    }else if(data.ok){
        alert("Token enviado com sucesso")
        sessionStorage.setItem("email", email);
        window.location.href = "./validarToken.html"
    }
    }catch(e){
        errorP.innerHTML = 'Ocorreu um erro. Tente novamente'
        errorP.style.color = 'red'
    };
});