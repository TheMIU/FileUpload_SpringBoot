const form = document.querySelector('form');
const progressBar = document.querySelector('#progressBar');

form.addEventListener('submit', (e) => {
    e.preventDefault();

    fetch('/file-upload/upload', {
        method: 'POST',
    })
        .then((response) => {
            if (response.ok) {
                console.log('Upload successful');
            } else {
                console.error('Upload failed');
            }
        })
        .catch((error) => {
            console.error(error);
        });
});
