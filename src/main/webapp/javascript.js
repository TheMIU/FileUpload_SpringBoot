const fileInput = document.getElementById('fileInput');
const previewImage = document.getElementById('preview');
const uploadedImage = document.getElementById('uploadedImage');

fileInput.addEventListener('change', function () {
    const file = fileInput.files[0];

    if (file) {
        console.log(file);
        console.log('Selected file: ' + file.name);

        // Read the selected file and display it in the preview
        const reader = new FileReader();
        reader.onload = function (e) {
            previewImage.style.display = 'block';
            previewImage.src = e.target.result;
        };
        reader.readAsDataURL(file);

        // Reset the progress bar
        $('#progressBar').val(0);
    }

    if (!file) {
        $('#progressBar').val(0);
        previewImage.style.display = 'none';
        uploadedImage.style.display = 'none';
    }
});

$(document).ready(function () {
    $('#fileUploadForm').submit(function (e) {
        e.preventDefault();
        const formData = new FormData(this);

        if (fileInput.files.length === 0) {
            alert('Please select a file before uploading.');
            return; // Don't proceed with the upload if no file is selected
        }
        
        $.ajax({
            url: '/file-upload/upload',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            xhr: function () {
                const xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener("progress", function (evt) {
                    console.log(evt)
                    if (evt.lengthComputable) {
                        const percentComplete = (evt.loaded / evt.total) * 100;
                        $('#progressBar').val(percentComplete);
                    }else {
                        $('#progressBar').val(0);
                    }
                }, false);
                return xhr;
            },
            success: function (response) {
                $('#progressBar').val(100); // Set the progress bar to 100% after completion
                alert("Upload success");
                uploadedImage.style.display = 'block';
            },
            error: function (error) {
                alert("Upload failed " + error.responseText);
            }
        });
    });
});


/*
$(document).ready(function () {
    $('#fileUploadForm').submit(function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        $.ajax({
            url: '/file-upload/upload',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                alert("Upload success " + response);
            },
            error: function (error) {
                alert("Upload failed " + error.responseText);
            }
        });
    });
});*/
