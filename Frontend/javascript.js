const fileInput = document.getElementById('fileInput');
const previewImage = document.getElementById('preview');
/*const uploadedImage = document.getElementById('uploadedImage');*/
let baseURL = "http://localhost:8080/"
let filename;

loadAndDisplayImages();

fileInput.addEventListener('change', function () {

    const file = fileInput.files[0];
    if (file) {
        console.log(file);
        filename = file.name
        console.log('Selected file: ' + filename);

        // Read the selected file and display it in the preview
        const reader = new FileReader();
        reader.onload = function (e) {
            previewImage.style.display = 'block';
            /*  uploadedImage.style.display = 'none';*/
            previewImage.src = e.target.result;
        };
        reader.readAsDataURL(file);

        // Reset the progress bar
        $('#progressBar').val(0);
    }

    if (!file) {
        $('#progressBar').val(0);
        previewImage.style.display = 'none';
        /*  uploadedImage.style.display = 'none';*/
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
            url: baseURL + 'file-upload/upload',
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
                    } else {
                        $('#progressBar').val(0);
                    }
                }, false);
                return xhr;
            },
            success: function (response) {
                $('#progressBar').val(100);

                loadAndDisplayImages();
                /*  const imageUrl = baseURL + 'file-upload/upload/' + filename;
                  $('#uploadedImage').attr('src', imageUrl);
                  $('#uploadedImage').show(); // Show the uploaded image*/
            },
            error: function (error) {
                alert("Upload failed " + error.responseText);
            }
        });



    });
});

// Function to load and display images
function loadAndDisplayImages() {
    $.ajax({
        url: baseURL + 'file-upload/list-images',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            const imageTable = $('#image-table');
            imageTable.empty();
            data.forEach(function (image, index) {
                const newRow = `
                        <tr>
                            <th scope="row">${index + 1}</th>
                            <td>${image.name}</td>
                            <td><img src= "http://localhost:8080/file-upload/upload/${image.name}" alt="Image" style="max-width: 100px;"></td>
                        </tr>
                    `;

                imageTable.append(newRow);
            });
        },
        error: function (error) {
            alert("Failed to load images: " + error.responseText);
        }
    });
}