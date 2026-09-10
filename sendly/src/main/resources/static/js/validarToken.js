const form = document.querySelector('form');
const errorP = document.querySelector('#errorP');

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        const dados = new FormData(form);
        const token = dados.get('token');

        const data = await fetch('https://sendly-production-b679.up.railway.app/cadastroUpdate2?token=' + token);
        if (data.ok) {
            window.href = "./";
        }
    } catch (e) {
        errorP.innerHTML = 'Token incorreto. Tente novamente'
        errorP.style.color = 'red'
    };
});