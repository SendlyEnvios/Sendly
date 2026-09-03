form.addEventListener('submit', async (e) => {
    
    e.preventDefault();

    const dados = new FormData(form);

    await fetch('https://pwjob-production-1606.up.railway.app/cadastroUpdate',{method: 'POST', body: dados});
});