const form = document.querySelector('form');
form.addEventListener('submit', async (e) => {
    
    e.preventDefault();

    const dados = new FormData(form);

    const data = await fetch('https://sendly-production-b679.up.railway.app/cadastroNew',{method: 'POST', body: dados});

    if(data.ok){
        window.location.href = "./index.html"
    }
});