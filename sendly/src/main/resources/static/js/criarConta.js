form.addEventListener('submit', async (e) => {
    
    e.preventDefault();

    const dados = new FormData(form);

    await fetch('https://sendly-production-b679.up.railway.app/cadastroNew',{method: 'POST', body: dados});
});