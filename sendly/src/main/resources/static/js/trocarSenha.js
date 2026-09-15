const token = sessionStorage.getItem("token");
const form = document.querySelector('form');
form.addEventListener('submit', async (e) => {
    
    e.preventDefault();

    const dados = new FormData(form);
    dados.append('token', token);
    try{
        const data = await fetch('https://sendly-production-b679.up.railway.app/cadastroUpdate3',{method: 'POST', body: dados});
        
        if(data.ok){
            alert("Sua senha foi alterada com sucesso");
            window.location.href = "./index.html"
        }
    }catch(e){
        errorP.innerHTML = 'Ocorreu um erro. Tente novamente'
        errorP.style.color = 'red'
    }
});