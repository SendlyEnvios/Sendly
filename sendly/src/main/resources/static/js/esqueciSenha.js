const form = document.querySelector('form');
form.addEventListener('submit', async (e) => {
    
    e.preventDefault();

    const dados = new FormData(form);

    await fetch('https://sendly-production-b679.up.railway.app/cadastroUpdate',{method: 'POST', body: dados});
});